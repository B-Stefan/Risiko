Risiko
======

A port of the popular board game "Risiko" in Java

Classes
======
The following paragraph list some todos for our project.

##Player

* public Country getCountries
* public void addCountires

##Country

* private Player Owner
* public void setOwner (Player newOwner)
* public Player getOwner ()

##Dice
* public compareTo(Object otherObject)
- Method for comparison of 2 Dices


##Turn
* private Player currentPlayer ()
* private void nextPlayer ()

##Fight
*public Fight (Country from, Country to)



##Order
Bei einem Auftrag müsste es 2 Unterklassen geben die die Schnittstele IOrder implementieren

1. Auftrag einen anderen Spieler zu vernichten
2. Auftrag 2 Kontinente zu besetzten


#Struktur
Wir folgten bei der Strukturierung grundsätzlich den Richtlinien des Apache Marvens Project
(http://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html#





