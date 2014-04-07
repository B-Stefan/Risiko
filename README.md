Risiko
======

A port of the popular board game "Risiko" in Java

Klassen
======
Folgende Klassen müssen mit entsprechenden Methdoen umgesetzt werden

##Player

* public Country getCountries
* public void addCountires

#Turn
    * private Player currentPlayer ()
    * private void nextPlayer ()

#Fight
    * public Fight (Country from, Country to)

#Order
Bei einem Auftrag müsste es 2 unterklassen geben die die Schnittstele IOrder implementieren

1. Auftrag einen anderen Spieler zu vernichten
2. Auftrag 2 Kontinente zu besetzten


Struktur
======
Wir folgten bei der Strukturierung grundsätzlich den Richtlinien des Apache Marvens Project
(http://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html#





