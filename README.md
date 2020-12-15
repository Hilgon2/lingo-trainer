# Lingo trainer API
![test coverage](https://github.com/Hilgon2/lingo-trainer/blob/development/.github/badges/jacoco.svg)

Voor het vak Backend Programming moeten wij een Lingo trainer maken. Deze trainer biedt de gebruiker een tool aan om te kijken of die het in zich heeft om mee te kunnen doen aan het geweldige spelshow Lingo! 

Elke beurt begint met een 5-letter woord. Wanneer deze goed is, wordt dit een 6-letter woord, daarna een 7-letter woord en vervolgens opnieuw een 5-letter woord. 
Het raden van een beurt heeft ook een aantal regels:
Een beurt is alleen geldig als er binnen 10 seconden wordt gereageerd en het te raden woord aan de volgende eisen voldoet:
* Het woord bestaat
* Het woord is juist gespeld
* Het woord bestaat uit het gegeven aantal letters
* Het woord schrijf je niet met een hoofdletter, zoals plaats- en eigennamen
* Het woord bevat geen leestekens, zoals apostrofs, koppelstreepjes en punten

Vervolgens krijgt de gebruiker per letter feedback op basis van het ingevoerde woord. Een ongeldige beurt levert enkel de feedback op dat het hele woord ongeldig is. Het kost dan ook een raadbeurt. Dit geldt ook voor het te laat reageren.

Een spel bestaat in principe uit een onbeperkt aantal rondes. Tijdens een ronde moet er een woord geraden worden in 5 beurten. Na elke raadbeurt krijgt een speler feedback, per letter, op het raden. Aan het begin van de ronde wordt de eerste letter gegeven.

Om Lingo te kunnen spelen hebben we geldige 5-, 6- en 7-letterwoorden nodig.
Hiervoor willen we een data-import kunnen doen.

Deze import moet aan een aantal voorwaarden voldoen (dit is goed te testen):

Het woord bestaat uit het gegeven aantal letters (5, 6 of 7 letters)
Het woord schrijf je niet met een hoofdletter, zoals plaats- en eigennamen
Het woord bevat geen leestekens, zoals apostrofs, koppelstreepjes en punten
