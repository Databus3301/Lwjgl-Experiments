<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>

---

<div style="font-variant:small-caps;text-align:center"> Projektdokumenation</div>
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

**Main.java:**
Die Main Methode ist der Einstiegspunkt in das Programm. Es erstellt ein neues Fenster und initialisiert es, worauf es die ```window.run``` ausführt welches die Hauptschleife vom Spiel startet.

<p style="font-variant:small-caps" align="center">«-<—>-»</p> 

**Audio Paket:**
Dieses Paket enthält Klassen, die für die Audiofunktionen im Spiel verantwortlich sind. Es verwendet die OpenAL Bibliothek, um Audio im Spiel zu spielen.  

<p style="font-variant:small-caps" align="center">«-<—>-»</p> 

**Render Paket:**
Zusammen mit dem Audio Packet und der Test-Klasse bildet das Render-Paket sämtlichen Hintergrundverwaltung von OpenGL, OpenAL und ihren Abstraktionen in handlichere Basiseinheiten wie ```Entity2D```, ```Interactable```, ```Shader``` oder ```Texture```.
Dieses Paket alleine stellt also das Pendant zur gemeinsam entwickelten Spieleumgebung bzw.  populäreren Game-Engines wie Godot oder Unreal.

Dieses Paket enthält vornehmlich Klassen, die ein funktionales und einfaches Rendern und Verwalten jeglicher Grafiken sichern. Dazu gehört die ```Window``` Klasse, die das Hauptfenster des Spiels erstellt und verwaltet, sowie die ```Renderer``` Klasse, die für das Zeichnen von Spielobjekten auf dem Bildschirm verantwortlich ist.  Weitere Unterkategorien sind die ```Camera```, die neben Kameraverwaltung auch dafür sorgt das der Spieler immer mittig bleibt sowie die ```Interactabels```, Eine Ansammlung an Klassen. die ```Entity2D```'s um Interagierbarkeit, wie des Klickens, Hoverns, Antippens und Ziehens erweitert. 

Darüber hinaus befindet sich im Render-Package die Unterkategorie ```Meshdata```, die Klassen, die zur Veränderung von allem visuell wahrnehmbaren da sind, verwaltet.  
Eine der wichtigsten Klassen des package ist die ```Entity2D``` Klasse. Sie gibt durch vererbung an andere Klassen einen Grundbaustein, an dem sich eine sehr große Anzahl an Klassen orientieren, so ist jeder Gegner, der Spieler, jedes Projektil ein Objekt dessen Klasse von Entity2D abstammt.  

<p style="font-variant:small-caps" align="center">«-<—>-»</p> 

**Game Paket:**
Dieses Paket enthält aufbauend auf dem Render-/Audio-Paket spielspezifische Klassen, die die Spiellogik und den Spielzustand auf einer Ebene verwalten, die sich nicht für eine verallgemeinerte Implementation im Renderpaket geeignet haben ( ```Dungeon``` , ```Room``` oder ```Homing```). 

Das Game Paket unterteilt sich wiederhohlt in zwei Kategorien. ```Action``` und die ```Entities```. 

Die Entities Kategorie verwaltet verschiedene Klassen die meistens von ```Entitiy2D```  erben, und sich über Vererbung oder Interfaces weiter in z.B. "lebend" und "fähig" unterteilen. 

Die Action Kategorie verwaltet Klassen, die auf zurückgreifen auf die Entities leben in das Spiel hauchen. Beispielhaft dafür ist der Erscheinungsprozess von Gegnern über ```Wave``` und ```EnemySpawner``` oder ```Ability``` und ```Upgrade``` die Gegner wie Spielern modulbasiert Fähigkeiten zuschreiben, um etwa ein ```Projectile``` zu schießen oder mit der _Dash-Ability_ aus der Abilitysammlung ```Abilities``` einen kleinen Teleport in Laufrichtung auszuführen.

<p style="font-variant:small-caps" align="center">«-<—>-»</p> 

**Tests Paket:**
Dieses Paket enthält verschiedene Testklassen, die vorrangig des Funktionstestens dienten, im späteren Verlauf aber immer mehr zu klasisch verwaltbaren Szenen wurden wie man sie aus Godot oder Unity kennt. Sie sind also ladbare Bestandteile etwas Größerem die abgeleitet von ```Test``` die autarken Szenen eines Spiels, wie etwas einen Hauptbildschirm, ein Einstellungsmenü oder einen Game-Over-Screen, darstellen und organisieren.

Wesentliche Test / Szenen unseres Spiels zählen den AbilitySelectionScreen, TestGame, TestStartScreen und TestGameOverScreen. Die Restlichen Szenen waren meistens Experimente oder Tests um Mechaniken auszuprobieren, wie zum Beispiel ein Test um die Kollisionskontrolle zu überprüfen. 

<p style="font-variant:small-caps" align="center">«-<—>-»</p> 

**"Res"-Ordner:**
Der *Res* Ordner enthält alle Ressourcen oder Assets die vom Spiel über Java-Quellcode hinaus benötigt werden. Darunter zählen ```textures``` für Spieler, Text und Räume, 3D / 2D ```models``` die aus Blender geladen werden können um Komplexere Formen abzubilden,  ```audio```-clips für Musik und Effekte, ```shaders``` für Programme auf Graphikkartenseite zum richtigen Interpretieren von Punktpositionen und RGB-Buffern, sowie die ```Settings``` zum programmausführungsübergreifenden Einstellungsverwalten.

<br><br><br>
<!--- Pagebreak --->
<div style="page-break-after: always"></div>


<div style="text-align:right">2-Implementation</div>

---

#### 3 - Implementation und Techniken

**Ausmaße:**

Die abertausende Zeilen an Quellcode einzeln zu analysieren würde ein Ausmaß von mindestens genauso vielen Seiten verlangen.

![](https://cdn.discordapp.com/attachments/801184991316148254/1246957936576565298/image.png?ex=665e4793&is=665cf613&hm=c1b9862696b43d59dca27c08723681cc6e4537a82085e1dd63f33692ff40a40b&)
<sub>Code-Frequency Analyse von [GitHub](https://github.com/Databus3301/Lwjgl-Experiments/graphs/code-frequency)</sub>

Und da es zum Glück auch nur um Schlüsselstellen geht ist das auch nicht weiter schlimm. Es ist allerdings ganz hilfreich sich vor trotz fehlender Dokumentation von jeder Nische vor die Augen zu rufen um welches Ausmaß es sich hier handelt.

Was gegeben war, waren die 115 Zeilen minimalstem [Startercodes](https://www.lwjgl.org/guide)  aus der LWJGL Dokumentation. Darin enthalten: 
- Betriebssystemunabhängiges erschaffen eines Fensters
- Weiterleiten von Tastatur- und Mauseingaben an Programmschnittstellen 
- Und Aufsetzen des *OpenGL* Graphik-Standarts der es unter anderem ermöglicht Buffer an eine Graphikkarte zu Schicken und dieser von Grund auf zu beschreiben wie dieser Interpretiert werden soll
Dazu kommt die Mathe-Bibliothek *JOML* die lineare Algebra auf Vektoren und Matrizen ermöglicht und *OpenAL* als Standart für die Schnittstelle zu den Lautsprechern.

ALLES Andere hinweg über ```Shader```, ```Texture```, ```VertexBuffer```, ```IndexBuffer```, ```ObjModelParser```, ```Font```, ```TextureAtlas``` und ```Button```, ```Slider``` oder ```Entity2D``` und jeder anderen ```src``` Datei ist auf der oben beschrieben Basis unter Anwendung von Computer-Graphik Methodik und ganz viel Mathematik entstanden und sprengt damit schon jeglichen Rahmen. 


<p style="font-variant:small-caps" align="center">«-<—>-»</p> 