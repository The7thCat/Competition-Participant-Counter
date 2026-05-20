# Competition Participant Counter

A Java application that automatically scrapes participant registration data from a public event website and counts registrations by competition category (`DISTANTS`).

It is designed for public Google Sheets-based registration lists linked from an event page. Just a finger excercise for basic java.
The problem is that the registration slams everyone into a google docs formular, so you don't really know how many participants
are registered for your race distance.

So this is a lazy written solution to this issue. Shame on me, everything inside main method. If I'll continue toying with it, it'll get it's propper structure.

---

# Features

- Scrapes event webpage for registration link
- Extracts Google Sheets registration URL automatically
- Converts Google Sheets `pubhtml` link into CSV export URL
- Downloads and parses CSV data
- Counts participants by `DISTANTS` category
- Sorts results by frequency (descending)
- Handles UTF-8 encoded data (Estonian characters)

---

# Requirements

- Java 21
- Maven 3.9+

All dependencies are managed by Maven.

---

# Tech Stack

- Java 21
- Maven 3.9+
- Jsoup (HTML scraping and parsing)
- Apache Commons CSV (CSV parsing)
- Java HttpClient (built-in HTTP client)

---

# How it works

## 1. Scrape event page

The application starts from an event page, for example:

https://vorumaaspordiliit.ee/uritused/jooksusari/

It searches the page for the link text:

INTERNETIS REGISTREERUNUTE NIMEKIRI

---

## 2. Extract Google Sheets URL

Example extracted URL:

https://docs.google.com/spreadsheets/d/e/.../pubhtml?gid=123&single=true

---

## 3. Convert to CSV export URL

The application converts it into:

https://docs.google.com/spreadsheets/d/e/.../pub?gid=123&single=true&output=csv

---

## 4. Download + parse CSV

The CSV is parsed using Apache Commons CSV and grouped by the column:

DISTANTS

---

## 5. Aggregate results

Each row is counted into its corresponding category.

---

# Example output

=== RESULTS ===  
Põhidistants MN16+: 180  
Lühem võistlusdistants NM12-14: 77  
1. mudilaste jooks-kõnd: 66  
Pikem kõnnidistants: 63  
Minidistants: 44  

---

# Run

## Build

mvn clean compile

## Run

mvn exec:java

(or run directly in IntelliJ IDEA)

---

# Project structure
```
src/  
└── main/  
    └── java/  
        └── AppStarter.java
```
---

# Notes

- The Google Sheet must be publicly accessible
- The column `DISTANTS` must exist in the CSV
- UTF-8 encoding is required for correct Estonian characters
- The scraper depends on stable link text on the event page
