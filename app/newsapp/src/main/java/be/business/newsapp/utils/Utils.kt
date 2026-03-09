package be.business.newsapp.utils

data class Countries(val countryName: String, val countryCode: String)

val countries = listOf(
    Countries("United States", "us"),
    Countries("India", "in"),
    Countries("United Kingdom", "gb"),
    Countries("Canada", "ca"),
    Countries("Germany", "de"),
    Countries("France", "fr"),
    Countries("Italy", "it"),
    Countries("Australia", "au"),
)
