package com.example.chatappnative.Util

object ValidatorUtil {
    private fun isNotEmail(value: Any?): Boolean {
        return !Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
            .matches(value.toString())
    }

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

    fun validateEmail(value: String): String {
        if (value.isEmpty()) {
            return "Trường này bắt buộc";
        }

        if (isNotEmail(value)) {
            return "Xin hãy kiểm tra lại định dạng email của bạn!"
        }

        return "";
    }
}