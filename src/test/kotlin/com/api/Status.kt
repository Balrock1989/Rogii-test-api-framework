package com.api

enum class Status(val code: Int) {
    OK(200),
    CREATED(201),
    NO_CONTENT (204),
    REDIRECTED(302),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404);
}