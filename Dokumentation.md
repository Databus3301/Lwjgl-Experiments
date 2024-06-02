<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>

---

<div style="font-variant:small-caps;text-align:center"> Projektsdokumenation</div>
<div style="font-variant:small-caps;text-align:center"> 2024</div>
<div style="font-variant:small-caps;text-align:center">PXIF1 - DR</div>
<div style="visibility:hidden">a</div>
<div style="font-variant:small-caps;text-align:center"> Cornelius Bonn & Felix Rose</div>

---

<br><br><br><br>

<!--- Pagebreak --->
<div style="page-break-after: always"></div>


<div style="text-align:right">1-Projektbeschreibung</div>

---

#### 1 - Projektbeschreibung/Zielsetzung/Anforderungen an das Projekt
Das Ziel des Projekts war es, ein einfaches 2D-Spiel zu erstellen, das auf dem Konzept eines Vampire-Suvivors-Like basiert. Das Spiel sollte eine Vielzahl von Funktionen und Mechaniken enthalten, die es dem Spieler ermöglichen, verschiedene Arten von Gegnern zu bekämpfen, verschiedene Waffen und Fähigkeiten zu verwenden und verschiedene Level erreichen.

Beispiele für Vampire-survivor-likes:

Vampire Surviviors:
![img](https://www.google.com/url?sa=i&url=https%3A%2F%2Fstore.steampowered.com%2Fapp%2F1794680%2FVampire_Survivors%2F%3Fl%3Dgerman&psig=AOvVaw0rq2bjENvPxpmk43yCV4Yb&ust=1717449002421000&source=images&cd=vfe&opi=89978449&ved=0CBIQjRxqFwoTCOiShJ_qvYYDFQAAAAAdAAAAABAE)

Brotato:
![img](https://www.google.com/url?sa=i&url=https%3A%2F%2Fstore.steampowered.com%2Fapp%2F1942280%2FBrotato%2F%3Fl%3Dgerman%26curator_clanid%3D4218320&psig=AOvVaw2vImgqC4Ghyb-cTN0gvtta&ust=1717449090993000&source=images&cd=vfe&opi=89978449&ved=0CBIQjRxqFwoTCNiLksrqvYYDFQAAAAAdAAAAABAJ)

<p style="font-variant:small-caps" align="center">«-<—>-»</p> 

Das Ziel des Spiels ist es so weit wie nur möglich in den Dungeon vorzudringen. Ursprünglich war es zwar die Idee einen Vampire-Suvivors-Like zu kreieren, nach einiger Zeit haben wir uns für diese Art von Spiel mehr begeistern können und haben uns deshalb dazu entschieden den Wellenbasierten Ansatz auf einer einzigen Map zu verwerfen und dafür den Ansatz zu wählen, dass man durch verschiedene Räume muss und in jedem eine gewisse Anzahl Gegner zu besiegen hat. 

<p style="font-variant:small-caps" align="center">«-<—>-»</p> 

Dass man durch ein Level-Up-System seine Fähigkeiten zu verbessern oder neue erhalten kann, war für uns aber schon von Anfang an im Spiel vorgesehen. 
Wir hatten uns das Spiel zuerst farbenfroher vorgestellt mit einer detailliert designten Map allerdings haben wir im verlauf gemerkt, dass wenn wir uns weniger auf die Grafiken konzentrieren mehr noch auf die Spielmechaniken konzentrieren können. Dazu kam, dass wir gefallen an dem "einfachen" Stil unseres Spiel gefunden haben und deshalb uns unter anderem auch dazu entschieden haben die Gegner bei farbigen Vierecken zu belassen. Der einfache Stil war zwar Ursprünglich nicht geplant aber gibt unserer Meinung nach dem Spiel einen eigenen Charm der den Fokus mehr darauf legt, Fortschritt im Spiel zu erzielen, und weniger darauf sich schön designte Aspekte anzusehen.
Außerdem hilft das simple Design dabei, sich mit den Projektilen zurechtzufinden, da diese in unserem Spiel teilweise sehr viele werden könnten, somit wird der Fokus mehr auf kompetitives Spielen gelegt als auf schönes Design. 

Beispiel Upgrade Auswahl:
!img[]()

Beispiel Gegner:
!img[]()

<p style="font-variant:small-caps" align="center">«-<—>-»</p> 

Die Umsetzung mit einem Start und Game Over Screen war auch von Beginn an festgelegt, wobei man sagen muss, dass die Idee zuvor noch ein wenig ausgebauter war als sie jetzt schlussendlich geworden ist. wir hatten überlegt Collectibels zu implementieren, die auch über mehrere Runden verteilt gespeichert werden und einem entweder Vorteile bringen oder nur dem Sammlelaspekt dienen.Auch ein hub war in Überlegung also ein Ort wo man zwischen den Runden zeit ferbringen kann, allerdings mussten wir beide Ideen verwerfen, da wir uns auf das wesentliche konzentriert haben und uns die Zeit fehlte um diese Ideen zu implementieren.
Was wir uns aber vor allem von dem Projekt erhofft haben, war es viel ausprobieren zu können und viel zu experimentieren, was man denke ich mal an der riesigen Menge an verschiedenen Tests sieht.

Startscreen:
![img]()

GameoverScreen:
![img]()

<!--- Pagebreak --->
<div style="page-break-after: always"></div>


<div style="text-align:right">2-Struktur</div>

---

#### 2 - Struktur
**Res Ordner:**
Der Res Ordner enthält alle Ressourcen, die das Spiel benötigt, wie z.B. die Texturen für den Schmied. Die Ressourcen sind in verschiedene Unterordner unterteilt, um sie besser zu organisieren. Die Unterordner sind: Fonts, Images, Music, Shaders und Sounds.

**Main.java:**
Die Main Methode ist der Einstiegspunkt in das Programm. Es erstellt ein neues Fenster und initialisiert es, worauf es die ```window.run``` ausführt welches die Hauptschleife vom Spiel startet.

<p style="font-variant:small-caps" align="center">«-<—>-»</p> 

**Render Paket:**
Dieses Paket enthält Klassen, die für das Rendern von Grafiken in Ihrem 
Spiel verantwortlich sind. Dazu gehört die Window Klasse, die das Hauptfenster des Spiels erstellt und verwaltet, sowie die Renderer Klasse, die für das Zeichnen von Spielobjekten auf dem Bildschirm verantwortlich ist.  Weitere unterkategorien sind die Camera, die wie der Name bereits verrät die Kamera verwaltet und dafür sorgt das der Spieler immer mittig bleibt und die Interactabels, Eine Ansammlung an Klassen, die dafür da sind, dass man verschiedene Dinge auf dem Bildschirm anzeigen lassen kann, mit denen man entweder Interagieren kann oder durch eine Interaktion zum Vorschein kommen. Des Weiteren befindet sich im Render package die Unterkategorie Meshdata, die Klassen, die zur veränderung von allem visuell wahrnehmbaren da sind, verwaltet.  
Eine der wichtigsten Klassen des package ist die Entity2D Klasse. Sie gibt durch vererbung an andere Klassen einen Grundbaustein, an dem sich eine sehr große Anzahl an Klassen orientieren, so ist jeder Gegner, der Spieler, jedes Projektil ein Objekt dessen Klasse von Entity2D abstammt.  

<p style="font-variant:small-caps" align="center">«-<—>-»</p> 

**Game Paket:**
Dieses Paket enthält Klassen, die die Spiellogik und den Spielzustand verwalten. Das Game Paket wird noch einmal unterteilt in zwei Kategorien. Die Action und die Entities Kategorie. Die Entities Kategorie verwaltet sehr viele verschiedene Klassen die von Entitiy 2D meistens abstammen, und unterteilt alle Entitiys noch einmal in Unterklassen wie living oder collectible. Die Action Kategorie verwaltet Klassen, die dann die Entities aus Entities nehmen und ihnen eine logistische aufgabe geben, indem sie zum Beispiel eine ENemyspawner lasse verwaltet, die wiederum alle Gegner die erscheinen verwaltet. Oder die Klasse Abillity die dafür sorgt, dass man Projectile immer in einem gewissen Rhythmus verschießen kann.

<p style="font-variant:small-caps" align="center">«-<—>-»</p> 

**Audio Paket:**
Dieses Paket enthält Klassen, die für die Audiofunktionen i, Spiel verantwortlich sind. Es verwendet die OpenAL Bibliothek, um Audio im Spiel zu spielen.  

<p style="font-variant:small-caps" align="center">«-<—>-»</p> 

**Tests Paket:**
Dieses Paket enthält verschiedene Testklassen, die verschiedene Aspekte des Spiels testen. Es würde keinen Sinn machen in der dokumentation alle Tests durchzugehen,deshalb werden wir uns auf die für das Spiel wesentlichen fokosieren. Die Restlichen Tests waren meistens Experimente oder Tests um Mechaniken auszuprobieren, wie zum Beispiel ein Test um die Kollisionskontrolle zu überprüfen. Zu den wichtigen Tests fürs Spiel gehören die AbilitySelectionScreen, TestGame, TestStartScreen und TestGameOverScreen Klassen.  

<!--- Pagebreak --->
<div style="page-break-after: always"></div>


<div style="text-align:right">2-Implementation</div>

---

#### 3 - Implementation