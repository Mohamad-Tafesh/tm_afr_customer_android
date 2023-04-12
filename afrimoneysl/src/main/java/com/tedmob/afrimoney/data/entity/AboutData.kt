package com.tedmob.afrimoney.data.entity

class AboutData(
    val socialLinks: List<SocialLink>,
    val text: String,
    val email: String,
) {
    class SocialLink(
        val image: Any?,
        val link: String,
    )
}