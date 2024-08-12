package com.example.chatappnative.Util

object ValidatorUtil {
    fun validateName(value: String): String {
        if (value.isEmpty()) {
            return "Trường này bắt buộc";
        }

        return "";
    }

    fun validatePassword(value: String): String {
        if (value.isEmpty()) {
            return "Trường này bắt buộc";
        }

        if (value.length < 6) {
            return "độ dài tối thiểu gồm 6 ký tự"
        }

        return "";
    }

    fun validatePhone(value: String): String {
        if (value.isEmpty()) {
            return "Trường này bắt buộc";
        }

        if (value.length !in 10..12) {
            return "Độ dài số điện thoại chỉ được 10 hoặc 11 kí tự"
        }

        return "";
    }
}