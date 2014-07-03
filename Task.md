
#Aufgabenblatt 1
- [x]   Eine „Welt“ stellt sich als zusammenhängendes Gebilde von aneinander grenzenden Ländern dar, d.h. sie ist im Grunde ein Graph von Nachbarschaftsbeziehungen. Jedes Land hat einen Namen, ein Kürzel und eine Armee (Menge von Einheiten), die genau einem Spieler gehört. Außerdem ist jedes Land einem Kontinent zugeordnet.
- [X]   Jeder Spieler besitzt eine Identität und kennt seine Mission (z.B. alle Länder eines Kontinents zu erobern).


##Spielzyklus
- [X] Neue Einheiten verteilen: Der Spieler erhält zu Beginn jeder Runde(in unserem Fall jedes Turns) in Abhängigkeit von
      den von ihm besetzten Ländern und Kontinenten neue Einheiten, die er verteilen muss.

- [x]   Angriff: Eingabe, mit welcher Teilarmee (wie viele Einheiten) von welchem eigenen Land aus welches andere Land angegriffen wird. Ein Spieler kann mit maximal drei Einheiten zurzeit angreifen. Mindestens eine Einheit muss im eigenen Land verbleiben und darf nicht für den Angriff genutzt werden.

- [X]   Verteidigung: Eingabe, mit welcher Teilarmee (wie vielen Einheiten) das angegriffene Land verteidigt wird. Ein Spieler kann sein Land mit maximal zwei Einheiten zurzeit verteidigen.
- [ ]   Bei echt(wenn gleich gewinnt Verteidiger) größerer Zahl ist die Verteidigungseinheit vernichtet, sonst die Angriffseinheit.
        Der Angreifer kann seine Angriffe bis zur endgültigen Eroberung des Landes fortsetzen oder abbrechen.


        da sich bei den Zwischenpräsentationen gezeigt hat, dass
        der genaue Ablauf beim Angriff im Risiko-Spiel noch Klärung
        bedarf, möchte ich heute die Regeln bzgl. Reihenfolge von
        Würfelwahl und Zufallszahlbestimmung konkretisieren (vgl.
        Regelheft im Ordner mit den Übungsaufgaben):
        1. Der Angreifer wählt die Anzahl der Armeen, mit denen er
        angreifen möchte (daraus ergibt sich, wie viele Würfel er
        verwendet, nämlich 1 bis 3).
        2. Die Werte der Würfel des Angreifers werden bestimmt und
        Angreifer und Verteidiger bekanntgegeben.
        3. Der Verteidiger entscheidet, ob er mit einem oder zwei
        Würfeln verteidigt (wobei zwei Würfel nur zulässig sind,
        wenn er auch über mindestens zwei Armeen in dem angegriffen
        Land verfügt).
        4. Die Werte der Würfel des Verteidigers werden bestimmt
        und bekanntgegeben.
        5. Die Würfel werden miteinander verglichen usw.
        ... Bei einem efolgreichen Angriff (d.h., wenn der
        Verteidiger seine letzte Armee in diesem Land verloren hat),
        muss der Angreifer mit mindestens so vielen Armeen in das
        Land einziehen, wie er beim letzten Angriff verwendet hat;
        darf aber auch beliebig viele weitere nachziehen, sofern
        noch mindestens eine zurückbleibt).


- [ ]   Einrücken: Wenn die Verteidigungsarmee vernichtet ist, muss mindestens eine Angriffseinheit in das eroberte Land einrücken. Wichtig ist, dass mindestens eine Einheit im Land des Angreifers verbleibt.
        Der Angreifer kann nur benachbarten Länder angreifen oder aber seine Eroberungsaktionen für diese Spielrunde(in unserem Fall Turn) beenden.
        Dabei wird sich nach den Regeln gerichtet
- [ ]   Verschieben von Einheiten: Eingabe, wie viele bisher unbeteiligte(nicht an einem Kampf beteiligt)  Einheiten von welchem eigenen Land in welches eigene Nachbarland verschoben werden sollen. Dabei muss immer mind. eine Einheit in dem Land zurückbleiben. Eine Einheit darf nur einmal pro Runde verschoben werden.
- [X]   Darüber hinaus können Informationen über die Länder abgefragt werden (Besitzer, Einheiten auf Land, vielleicht auch benachbarte Länder,) Rein Programmtechnisch.

- [X]   Damit der Anwendungskern getestet werden kann, soll zudem eine CUI („Command Line User Interface“) entwickelt werden, d.h. eine Möglichkeit zum Spielen von Risiko über die Kommandozeile. Das wird zugegebenermaßen kein schönes Spielen, ist aber zum Testen des Anwendungskerns unerlässlich.


#Aufgabenblatt 2
##Persistente Datenhaltung
- [ ]   Der aktuelle Spielstand soll persistent abgelegt werden.(>>>>>>>?<<<<<<<<)
- [X]   Überlegen Sie, welche Daten Sie speichern müssen, um das Spiel später nahtlos wieder fortsetzen zu können (z.B. Verteilung der Einheiten). Gehen Sie dabei davon aus, dass es sich um ein „Hot Seat“-Spiel handelt, die beteiligten Spieler des abzuspeichernden Spiels sitzen am selben Computer. Beim späteren Laden sitzen dieselben Spieler am Computer und wollen weiterspielen.
- [X]   Überlegen Sie, wie viele Dateien Sie für die Speicherung der Daten verwenden wollen und in welcher Form Sie die Daten in diesen Dateien ablegen.
        Bedenken Sie, dass Sie bei der Verwendung von Objektserialisierung keine Möglichkeit haben, Testdaten für Ihre Anwendung per Hand zu erstellen (z.B. Verteilung der Länder und von Einheiten auf Länder, Zuordnung von Missionen etc.) und Sie außerdem das Spiel nicht einfach an andere Spielwelten anpassen können (z.B. Bremer Stadtteile als Länder).
- [X]   Funktionen zum Speichern und Laden sollen über die CUI aufgerufen werden können; ein automatisches Laden und Speichern ist nicht gewünscht.


##Fehlerverarbeitung
- [X]   Ihr System soll fehlertoleranter werden, d.h. auch in ungewöhnlichen Situationen (z.B. Angriff mit mehr Einheiten als verfügbar, unzulässiges Verschieben von Einheiten etc.) nicht mit Absturz, sondern mit aussagekräftigen Fehlermeldungen reagieren.
- [X]   Definieren und verwenden Sie geeignete Exception-Klassen zur Repräsentation der verschiedenen Fehlersituationen (z.B. LaenderNichtBenachbartException) und verarbeiten Sie entsprechende Exceptions – wenn möglich – so, wie Sie es von einem benutzerfreundlichen System erwarten würden. Verlassen Sie sich nicht darauf, dass die CUI nur korrekte Methodenaufrufe durchführt oder nur „sinnvolle“ Benutzereingaben durchlässt!

##Funktionale Erweiterung
- [X]   Aufgaben („Missionen“) können vielfältig sein, z.B. „Erobern Sie 24 Provinzen / alle Provinzen eines Spielers / alle Provinzen eines Kontinents“ etc. Für jede dieser Aufgaben muss das System überprüfen können, ob die Aufgabe erfüllt wurde.
        Eine geeignete Vorgehensweise für diese (und ähnliche) Problemstellungen ist die folgende:
        o Leiten Sie verschiedene spezielle Unterklassen von einer generellen Klasse Mission ab
        o Setzen Sie zur Überprüfung der Erfüllung eine Methode boolean istErfuellt() ein und machen Sie Gebrauch von Polymorphie.
- [X]   „Einheitenkarten“: Ein Spieler erhält eine so genannte Einheitenkarte, wenn er in einer Runde eine Provinz erobert hat. Diese Einheitenkarten tragen Symbole (Reiter, Soldat, Kanone). Besitzt ein Spieler drei Karten mit demselben Symbol oder mit verschiedenen Symbolen, kann er die drei Karten gegen neue Einheiten eintauschen (Details, z.B. zur Anzahl der zusätzlichen Einheiten, bitte noch mal in den Regeln nachschlagen!).



#Aufgabenblatt 3

- [X]   Konzentrieren Sie sich zunächst auf Basisfunktionen wie
         o die Initialisierung der Mitspieler (Spielername, Farbe),
         o die Darstellung der Spielwelt inkl. der aktuellen Verteilung von Einheiten auf die
         Provinzen,
         o das Verteilen und Verschieben von Einheiten und o das Angreifen / Verteidigen von Provinzen.

- [X]   Erst wenn diese Basisfunktionen über die GUI verwendbar sind, sollten Sie an der Vervollständigung des Funktionsumfangs arbeiten (Missionen, Einheitenkarten, Anzeige von zusätzlichen Informationen zum Spielstand, (Keine Anzeige von Anzeige der Kontinente)).
- [ ]   Kleiner Tipp zur Zuordnung von Mausklicks zu Provinzen: Wenn Sie für die Auswahl von Provinzen beim Angriff Buttons oder ähnliches vermeiden wollen, können Sie auch direkt Mausklicks auf die Landkarte auswerten. Jedes MouseEvent hat x,y-Koordinaten, anhand derer sich der Farbwert an der entsprechende Position einer Grafik (der Weltkarte) auswerten lässt. Über den Farbwert kann man dann auf die angeklickte Provinz schließen. Dabei treten aber verschiedene Probleme auf:
         o Provinzen eines Kontinents haben vielleicht die gleiche Farbe: dann kann nur auf den Kontinent geschlossen werden und nicht auf die Provinz.
         o Eine Weltkarte soll vielleicht besonders schön sein und setzt dazu Farbverläufe ein (z.B. Vergilbung einer Weltkarte): dann ist einer Provinz keine eindeutige Farbe zugeordnet, sondern viele Farben.
         o Die Weltkarte ist mit den Namen von Kontinenten und Provinzen beschriftet(Tooltip oder Kontextmenü ist eine Beschriftung) : was ist, wenn der Klick nun genau auf einen Schriftzug zielte?
         Lösungsidee: Verwenden Sie einfach eine zweite Weltkarte, die niemals angezeigt wird und nur intern zur Auswertung von Mausklicks eingesetzt wird. Diese zweite Weltkarte enthält keinerlei Beschriftungen und jeder Provinz ist eine eindeutige Farbe zugeordnet. Dass das eine eher „hässliche“ Weltkarte wird, ist unwichtig – sieht ja keiner!
         Noch eine Anmerkung: es ist nicht zwingend erforderlich, dass Sie diesem Vorgehen folgen. Eine Weltkarte, bei der man die Provinzen über gesonderte Buttons oder andere Dialogelemente auswählt, tut’s auch.


#Aufgabenblatt 4


- [ ]   Implementieren Sie den Server.
         Hinweis für die Socket-Variante (wenn Sie sich bislang am groben Aufbau der
         Bibliotheksanwendung orientiert haben): Orientieren Sie sich am Bibliotheksserver
         o Übertragen Sie die Schichten Anwendungskern und Persistenz sowie die Datenstrukturen (Objekte, die von GUI, Anwendungskern und Persistenz benötigt werden) in ein gesondertes Server-Projekt
         o Ergänzen Sie eine Klasse XYServer, die Verbindungswünsche von Clients entgegennimmt (XY = eShop oder Risiko oder ...).
         o Ergänzen Sie eine Klasse ClientRequestProcessor, die für die Verarbeitung von Client-Anfragen gemäß dem Kommunikationsprotokoll zuständig ist und diese durch Methodenaufrufe auf dem Anwendungskern ausführt. Für jede Verbindung des Servers zu einem Client wird eine Instanz der Klasse erzeugt und als Thread gestartet.

- [ ]   Implementieren Sie den Client.
         Hinweis für die Socket-Variante (wenn Sie sich bislang am groben Aufbau der
         Bibliotheksanwendung orientiert haben): Orientieren Sie sich am Bibliotheksclient
         o Übertragen Sie die Schicht Benutzungsschnittstelle sowie die Datenstrukturen (s. o.) unverändert in ein gesondertes Client-Projekt
         o Wandeln Sie die Klasse der Schicht Anwendungskern, die als Hauptzugangsknoten zum Anwendungskern dient, in eine „Fassade“ für den Netzwerkzugriff um und ergänzen Sie diese in Ihrem Client-Projekt.
         Im Bibliotheksbeispiel existiert neben der Klasse Bibliotheksverwaltung auf dem Server noch eine Klasse BibliotheksFassade auf dem Client. Diese Klasse, die von der GUI verwendet wird, bietet dieselben Methoden an und implementiert die clientseitige Netzwerkkommunikation gemäß dem Kommunikationsprotokoll
        Analog dazu die RMI Varriante

- [X]   Überlegen Sie, wo Synchronisierungen des Zugriffs auf gemeinsame Datenstrukturen sinnvoll sind. Implementieren Sie diese. Erinnerung: mehrere nebenläufig ausgeführte Threads (sprich mehrere parallel verbundene Clients) können zu Konflikten beim Zugriff auf gemeinsame Datenstrukturen führen.



- [X]   Sowohl beim eShop als auch bei Risiko ist es sinnvoll, dass die Änderungen, die ein Client ausführt (z. B. Artikel kaufen oder Einheiten verschieben) automatisch und umgehend an alle anderen Clients gemeldet werden. Damit wird erreicht, dass andere Benutzer des Systems über den aktuellen Systemzustand informiert sind, ohne z. B. einen Refresh-Button drücken zu müssen.
        Für die Realisierung dieser Funktionalität gibt es im abschließenden Kolloquium Zusatzpunkte, mit denen andere evtl. vorhandene Schwächen Ihres Programms zumindest in Teilen aufgefangen werden können.
