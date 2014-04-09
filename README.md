Risiko
======

A port of the popular board game "Risiko" in Java

#Using Git

1. Install Git Command line (http://git-scm.com/downloads)
2. Open your Console/Terminal and navigate to your local project folder
3. execute 'git pull'

4. Start working on the game

5. After your're done execute following commands
    ```
    git add .
    git commit -m '<your comment>'
    git push
    ```


Classes / TODOS
======
The following paragraph list some todos for our project.


##Country
* public void setOwner (Army newOwner)
  - Beim setzten eines neuen Owners muss dieser auch eine Army mitbringen ansonsten geht dies nicht
* public Player getOwner ()


##Army
* public void setCurrentPosition(Country c)
* public Country getCurrentPosition ()


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





