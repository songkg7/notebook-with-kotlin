package com.example.demosns.router

import com.example.demosns.application.handler.FollowHandler
import com.example.demosns.application.handler.MemberHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class SnsRouters(
    private val memberHandler: MemberHandler,
    private val followHandler: FollowHandler
) {

    @Bean
    fun memberRouter() = coRouter {
        GET("/ping") {
            ServerResponse.ok().bodyValueAndAwait("pong")
        }
        POST("/member", memberHandler::create)
        GET("/members", memberHandler::findAll)

        "/member".nest {
            GET("/{id}", memberHandler::findById)
            PUT("/{id}", memberHandler::update)
            DELETE("/{id}") {
                TODO()
            }
        }

        onError<Exception> { e, _ ->
            ServerResponse.badRequest().bodyValueAndAwait(e.message ?: "error")
        }
    }

    @Bean
    fun followRouter() = coRouter {
        POST("/follow/{fromMemberId}/{toMemberId}", followHandler::create)

        onError<Exception> { e, _ ->
            ServerResponse.badRequest().bodyValueAndAwait(e.message ?: "error")
        }
    }
}
