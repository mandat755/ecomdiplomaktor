package org.example

import com.stripe.Stripe
import com.stripe.model.PaymentIntent
import com.stripe.param.PaymentIntentCreateParams
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

// Об'єкти для обміну даними з Android
@Serializable
data class PaymentRequest(val amount: Long)

@Serializable
data class PaymentResponse(val clientSecret: String)

fun main() {
    // Render автоматично дає порт у змінній PORT. Якщо її немає — 8080.
    val port = System.getenv("PORT")?.toInt() ?: 8080

    // Твій секретний ключ Stripe
    Stripe.apiKey = System.getenv("STRIPE_SECRET_KEY") ?: ""
    embeddedServer(Netty, port = port, host = "0.0.0.0") {
        // Налаштовуємо сервер на роботу з JSON
        install(ContentNegotiation) {
            json()
        }

        routing {
            // Тестовий шлях, щоб перевірити чи працює сервер
            get("/") {
                call.respondText("Сервер Stripe працює!")
            }

            // Шлях, до якого буде стукати Android застосунок
            post("/create-payment-intent") {
                try {
                    val request = call.receive<PaymentRequest>()

                    val params = PaymentIntentCreateParams.builder()
                        .setAmount(request.amount) // сума в центах
                        .setCurrency("usd")
                        .build()

                    val intent = PaymentIntent.create(params)

                    // Повертаємо clientSecret в Android
                    call.respond(PaymentResponse(intent.clientSecret))
                } catch (e: Exception) {
                    call.respondText("Помилка: ${e.message}", status = io.ktor.http.HttpStatusCode.InternalServerError)
                }
            }
        }
    }.start(wait = true)
}