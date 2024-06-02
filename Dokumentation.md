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
Das Ziel des Projekts war es, ein einfaches 2D-Spiel zu erstellen, das auf dem Konzept des klassischen Dungeon Crawlers basiert. Das Spiel sollte eine Vielzahl von Funktionen und Mechaniken enthalten, die es dem Spieler ermöglichen, verschiedene Arten von Gegnern zu bekämpfen, verschiedene Waffen und Fähigkeiten zu verwenden und verschiedene Level erreichen. 

<p style="font-variant:small-caps" align="center">«-<—>-»</p> 

Das Ziel des Spiels ist es so weit wie nur möglich in den Dungeon vorzudringen. Ursprünglich war es zwar die Idee einen Wellen basierten Vampire-Suvivors-Like zu kreieren, nach einiger Zeit haben wir uns für diese Art von Spiel mehr begeistern können und haben uns deshalb dazu entschieden den Wellenbasierten Ansatz auf einer einzigen Map zu verwerfen und dafür den Ansatz zu wählen, dass man durch verschiedene Räume muss und in jedem eine gewisse Anzahl Gegner zu besiegen hat. 

<p style="font-variant:small-caps" align="center">«-<—>-»</p> 

Dass man durch ein Level-Up-System seine Fähigkeiten zu verbessern oder neue erhalten kann, war für uns aber schon von Anfang an im Spiel vorgesehen. 
Wir hatten uns das Spiel zuerst farbenfroher vorgestellt mit einer detailliert designten Map allerdings haben wir im verlauf gemerkt, dass wenn wir uns weniger auf die Grafiken konzentrieren mehr noch auf die Spielmechaniken konzentrieren können. Dazu kam, dass wir gefallen an dem "einfachen" Stil unseres Spiel gefunden haben und deshalb uns unter anderem auch dazu entschieden haben die Gegner bei farbigen Vierecken zu belassen. Der einfache Stil war zwar Ursprünglich nicht geplant aber gibt unserer Meinung nach dem Spiel einen eigenen Charm der den Fokus mehr darauf legt, Fortschritt im Spiel zu erzielen, und weniger darauf sich schön designte Aspekte anzusehen.
Außerdem hilft das simple Design dabei, sich mit den Projektilen zurechtzufinden, da diese in unserem Spiel teilweise sehr viele werden könnten, somit wird der Fokus mehr auf kompetitives Spielen gelegt als auf schönes Design. 

<p style="font-variant:small-caps" align="center">«-<—>-»</p> 

Die Umsetzung mit einem Start und Game Over Screen war auch von Beginn an festgelegt, wobei man sagen muss, dass die Idee zuvor noch ein wenig ausgebauter war als sie jetzt schlussendlich geworden ist. wir hatten überlegt Collectibels zu implementieren, die auch über mehrere Runden verteilt gespeichert werden und einem entweder Vorteile bringen oder nur dem Sammlelaspekt dienen.Auch ein hub war in Überlegung also ein Ort wo man zwischen den Runden zeit ferbringen kann, allerdings mussten wir beide Ideen verwerfen, da wir uns auf das wesentliche konzentriert haben und uns die Zeit fehlte um diese Ideen zu implementieren.
Was wir uns aber vor allem von dem Projekt erhofft haben, war es viel ausprobieren zu können und viel zu experimentieren, was man denke ich mal an der riesigen Menge an verschiedenen Tests sieht.


<!--- Pagebreak --->
<div style="page-break-after: always"></div>


<div style="text-align:right">2-Struktur</div>

---

#### 2 - Struktur

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
Dieses Paket enthält verschiedene Testklassen, die verschiedene Aspekte Ihres Spiels testen. Es würde keinen Sinn machen in der documentation  alle Tests durchzugehen,deshalb werden wir uns auf die für das Spiel wesentlichen fokosieren. Die Restlichen Tests waren meistens Experimente oder Tests um Mechaniken auszuprobieren, wie zum Beispiel ein Test um die Kollisionskontrolle zu überprüfen. Zu den wichtigen Tests fürs Spiel gehören die AbilitySelectionScreen, TestGame, TestStartScreen und TestGameOverScreen Klassen.  


![img](https://cdn.discordapp.com/attachments/801184991316148254/1245063652504244254/image.png?ex=665dfae2&is=665ca962&hm=561a0e8eb0253d433f4ea4fa36925bf70d96777c1e088a661486c5c2a24ced50&)