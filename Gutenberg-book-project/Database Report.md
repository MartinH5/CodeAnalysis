
# Database Report

### Which database engines are used.

Vi fik til opgave at teste 3 database engines op imod hinanden. Til det skulle vi selv parse en masse data bestående af delvis Bøger og deres authors fra Project Gutenberg, samt en lang række byer fra et datasæt (Geonames). Derved skulle vi opsætte databaserne således, at de kunne finde den information så vi var interesseret i, heriblandt information vedrørende relationerne mellem de forskellige entiteter; bøger, forfattere og nævnte byer. Eftersom vi skulle udvikle 3 systemer, gav det kun mening at bruge alle 3 typer fra undervisningen; en Dokument-, SQL- og Graph database. MongoDB var det oplagte valg mht. dokument baseret, da de andre valg var ukendte for os. For SQL lå valget mellem MySQL, PostgreSQL og MariaDB - her faldt valget på MySQL, primært fordi vi har mest erfaring med den, og eftersom vi valgte at udviklede applikationen i Java, så har vi førhen spenderet mest tid med MySQL. Graph database valget blev Neo4j, som vi stiftede bekendtskab med i løbet af uddannelsen. 
How data is modeled in the database.
Først og fremmest modellerede vi databasen efter nogle uml’er specifikt lavet til hver database paradigme, hvilket kaldes den logiske data model. Den fysiske database model bliver lavet ud fra den logiske model og som er den der bliver brugt i databasen.
Her er de tre database modeller, startene med MySQL:

Til SQL handlede det om at få dataen struktureret på en meningsfuld måde uden at gøre relationer for komplicerede. Ud fra den data vi havde, oprettede vi først en model, der opfyldte alle kriterierne for opgaven. Strukturen endte op med at bestå af følgende tabeller og relationer:



![alt text](https://github.com/KongBoje/Gutenberg-book-project/blob/master/docs/image1.png "SQL")




Neo4j struktur er lidt anderledes i forhold til andre database paradigmer, denne paradigm modellere databasen efter grafer.

![alt text](https://github.com/KongBoje/Gutenberg-book-project/blob/master/docs/image3.png "Graph")

Dette er den logisk model af vores Neo4j database, modellen er lavet meget efter hvordan MySQL databasen blev modelleret. Vi har tre entiteter og to relationer, entiteterne værende Author med id og name, Book med id og titel og til sidst City med id, name, longitude og latitude. De to relationer har navnene Written og Mentions, Written relationen er mellem Author og Book og Mentions relationen er mellem Book og City. Disse entiteter og relationer blev importeret ned til den fysiske model via csv filer i Cypher, som er det sprog neo4j bruger. Først blev entiteterne importeret ned derefter fik de deres unikke indexer og constraints, da dette ville gøre importen af relationerne hurtigt, som blev importeret til sidst.
Efter alt var blevet importeret blev den fysiske model lavet.
MongoDBs Nosql struktur er lidt anderledes i forhold til det populære SQL modelleringssprog, og var i sin tid et forsøg på at lave et alternativ til den relationelle database. Nosql havde til formål at holde en simpel struktur over SQLs ofte mere komplicerede struktur. Dertil fås en bedre skalerende database, da den er mere effektiv til at holde meget data. Den er desuden mere passende for Agil softwareudvikling - da man i modsætning til SQL ikke skal have styr på en masse struktur forhenværende. 
Det krævede en masse tid og ressourcer at få dataen fra alle bøger lagt ned i et format der var til at arbejde med, så da vi originalt fik det lagt ned i MySQL gav det bedst mening at oprette vores MongoDB ud fra dataen fra SQL. Vi endte op med at oprette de samme collections, som MySQL havde i tables. Ud fra denne struktur udviklede vi nogle queries, der hentede den relevante data ned. Vi endte hurtigt op med fungerende queries, men hvert query var nødt til at lave flere kald til databasen for at få den relevante data, hvilket selvfølgelig ikke er optimalt. Senere fandt vi frem til, at de nyere udgaver af MongoDB faktisk supportere “Join-Queries” via aggregationer, hvilket vi forsøgte at implementere, men efter mange timer gav vi op, da det mildest talt var udfordrende at strukturer komplekse mongo queries i Java. Den struktur vi endte op med endte op med at ligne en SQL database, uden at udnytte de potentielle relationer, der forhen var sat op. 

MongoDB tilbyder både relationer og selvfølgelig de fordele, som medfører at være en Nosql database - bestående af dokumenter. En bedre og formentlig hurtigere struktur, ville være at samle dataen under en kollektion af bøger, med hver deres author(s) samt en liste af nævnte byer. Havde vi haft mere tid - ville vi have konverteret dataen fra databasen til en ny struktur der kunne have set sådan ud: 

![alt text](https://github.com/KongBoje/Gutenberg-book-project/blob/master/docs/image2.png "Mongo")
  
Denne struktur ville have et dokument per bog, med tilhørende author samt en liste af byer - der hver har et sæt af koordinater. Alle queries kunne tage udgangspunkt i denne ene kollektion, og gennem et enkelt kald til databasen, hente den ønskede information. Denne opstilling er desuden simpel og intuitiv, hvilket giver rig mulighed for skalering og videre udvikling.  

### How data is modeled in your application.

Originalt havde vi planlagt, at vores queries blot skulle returnere den aktuelle information, var vi interesserede i by nævnt i en bog, så kunne det blot være en liste af bynavne i form af strings. Senere besluttede vi, at modellere dataen i form af entities. Her oprettede vi én for hver query. Den måde sikrede vi os, at der var outputtet var konsistent på tværs af de tre databaser. Eksempelvis havde vi den første query, hvor målet var at finde alle de forfattere plus bøger, hvor en given by bliver nævnt, hvilket resulterede i en entitet bestående af ét author navn og én bogtitel. Ligeledes fik hvert query altså en entitetsklasse udstyret med felter for den information vi var interesseret i. Set i bakspejlet ville det have været bedre med mere generelle entiteter, hvis applikationen skulle videreudvikles - og eventuelt indeholde flere forskellige CRUD operation, end blot read. Derved kunne vi altså tilgå vores databaser igennem queries, der henter forbindelse igennem “Connection” klasserne, hvor tilbage svarer kunne modelleres og sendes op til en frontend. 


![alt text](https://github.com/KongBoje/Gutenberg-book-project/blob/master/docs/image4.png "Struktur")
  
### How the data is imported.

Der var mange ting der skulle importeres, og det skete i flere steps. Først
og fremmest, givent de queries der var krav om, kan man se at det er de følgende
ting man får brug for: Author, Book, City.
Til import af cities fik vi udleveret et dataset som skulle parses. Filen hedder
`cities15000.txt`, men den er faktisk en tabulator-separeret fil. Ved at kigge
lidt på filen, kan man finde de interessante kolonner:

|Kolonne|Værdi|
|---|---|
|3|Det engelske navn af en by|
|5|Latitude af en by|
|6|Longtitude af en by|
|15|Indbyggertallet af en by|

Der er ikke nogle af de queries der var krav om som har at gøre med
indbyggertallet af en by. Der er den problemstilling at der er flere byer
som har samme navn. Tilgangen til dette problem blev at sige, at i tilfælde
af navnekollisioner, fortolker man et bynavn til at hentyde til den by, hvori
der bor flest mennesker.
Til al import og parsing blev der brugt Python2 scripts.

For at importere byerne blev der lavet et script `city_parser.py`. Denne fil
indlæser `cities15000.txt` med `pandas` og importerer i en MariaDB-database via
`mysql.connector`-importen. I tilfælde af kollisioner overskriver den faktisk
latitude og longtitude med værdier parset som int. Hovsa!
Senere hen ville der blive brug for at parse byer som har navne der strækker
sig over flere ord, så tilgangen her blev at notere alle bynavne der er på
flere ord, og så at gemme det første ord i bynavnet som værende **interessant**.
Udsnit af `data_cities_lookup.py`:
```python
cities_maybe = {'Satara': 0, 'Yibin': 0, 'Rheinberg': 0, 'Dilling': 0, 'Kondapalle': 0, 'Constanta': 0,
...
'Lac': 0, 'Koprivnica': 0, 'Daura': 1, 'Smederevo': 0, 'Chur': 0, 'Panchla': 0, 'Behshahr': 0, 'Lat': '1',
'Nouakchott': 0, 'Malvern': '1', 'Wenling': 0, 'Basoko': 0, 'Lar': 0, 'Las': '1',
...
}
```
De interessante ord blev gemt i en fil ved navn `data_cities_lookup.py`. Man
vil så kunne bruge denne dictionary senere hen, for at vide, at når man støder
på et interessant ord som "Las", så kan det være at det er "Las Vegas".
Så er byerne blevet indsat, men man skal bruge deres id'er senere hen for at kunne
danne relationerne.
Der blev lavet et nyt Python2 script ved navn `import.py`. I den blev der
lavet en funktion som hedder `print_city_ids`, som bruges til at generere en
python dictionary med et bynavn og et SQL primary key int id. Denne dictionary
blev gemt som `data_cities_id.py`.

For at importere authors og books, er det ikke rigtigt muligt bare at parse
det fra en bogs indhold. Der er mange bøger hvori det ikke står, eller hvori
det står på en måde der ikke er i overensstemmelse med andre bøgers formater.
Istedet blev tilgangen at parse fra indholdet af den fil der hedder
`rdf-files.tar.bz2`. Dette viste sig at ikke være en bunke af almindelige
XML-filer, men et irreterende format der hedder RDF. Det blev nødvendigt
at bruge en python import som hedder `rdflib`. Det var ikke lige til at
finde ud af hvordan man kun skulle få fat i en `creator`, så tilgangen blev
at fortolke alle med navn som author. Det vil sige, at oversættere bliver
medregnet som co-creators i datasettet. RDF-filerne fulgte alle samme format
med et id i en undermappe `cache/epub/<id>/pg<id>.rdf`. Disse id-er
tilsvarer et fil-id fra gutenberg- datasettet, men gutenberg-bøgerne
kunne ligge under henholdsvis `books/<id>.txt` og `books/<id>/<id>.txt`.
Enkelte af dem havde et filnavn med bogstaver - disse blev ignoreret.

Der var flere filer i RDF-filsamlingen, så det måtte man tage sig af. Tilgangen
blev at sige, at forfatter- og bogparsningsdelen i `import.py` kun vil importere
hvis den kan finde en RDF-fils tilsvarende gutenberg bog-id.
Import af bøger og forfattere, og forfatter-bog-relationer skete i to forskellige
steps. Først for at indsætte dataen, og så for at lave opslag på dataen for at
få fat i id'erne og at lave relationerne.

Til sidst manglede kun parsing af bynavne og relationerne bogid:byid.
Python2-scriptet `city_finder.py` indlæser de i forvejen skabte dictionaries med
interessante bynavne, bynavne og by-id'er. Den går så igennem hver paragraf, og
checker om hvert ord er et interessant ord. Støder den på et interessant ord,
vil den checke om de næste 6 ord udgør et bynavn. Hvis ej, så check de næste
5 ord og så videre. Fandt den et bynavn på for eksempel 3 ord, vil den springe
tre ord frem i parsningsprocessen bagefter. Parseren fjerner irreterende
tegnsætningstegn udover bindestreger - disse bruges blandt andet i mange
mellemøstlige lande.
Der kommer en del false positives. I Tyrkiet findes der en by der hedder `Of`,
og i Polen findes der en by der hedder `Police`, og i Tjekkiet findes der en
by ved navn `Most`. Der er også false positives da `New York` ofte nævnes.
`New York` er ikke en by, så parseren finder `York` i det tilfælde. Når en by
er blevet fundet, vil parseren lave en relation i bog-by-relationstabellen.

Sådan blev author, author_book, book, book_city og city-tabellerne oprettet.
Senere hen viste det sig at være ret så langsomt at finde alle authors og
bøger der nævner en stor by som for eksempel `London`, så til optimering
blev der lavet en ekstra author-book-city-relationstabel. Der er mere
redundans, men nogle opslag blev langt hurtigere ved at bruge denne tabel
istedet. Senere viste det sig at query på authors og books der nævner en by via
JOINS var langsomme da der ikke var et index på city_t.name.
For at importere til de andre databaser, blev der eksporteret fra SQL til en
CSV, som de andre databaser kunne læse.

#### Diagram for import

Dette er et diagram over hvordan databasestrukturen først blev udtænkt.
![lortediagram.png](https://github.com/KongBoje/Gutenberg-book-project/blob/master/docs/lortediagram.png)

Her er abc-tabellen taget med.
![diagram2.png](https://github.com/KongBoje/Gutenberg-book-project/blob/master/docs/diagram2.png)

abc-tabellen er ikke nødvendig, og med et index på city_t.name er der ikke stor
forskel på køretiden med eller uden.


### Behavior of query test set

We decided to test each query for each database with the same datas

Vi besluttede at teste hver databases query med det samme datasæt. Det betyder, at vi kørte hver query 5 gange, med forskelligt input. Der blev i mellemtiden taget tid på, hvor lang tid det tog for hver database at gennemfører hvert sæt af queries. Eksempelvis startede vi timeren, hvorefter querien blev kørt én gang per data i datasættet, hvorefter timeren stoppes. Vi gentog denne proces 4 gange indtil vi havde indsamlet 4 stks. resultater. Resultatet blev gemt som en liste, hvor første index omhandlede første query, andet index holdte resultatet for anden query test osv. Testen blev kørt 10 gange med minimal udsving af resultatet. 

**Mongo Test Results: [9.745, 9.352, 14.884, 9.352]**

**MySQL Test Resultater: [7.186, 0.368, 0.372, 67.063]**

**Neo4J Test Resultater: [1.514, 0.404, 0.941, Not Done]**

Resultatet blev konverteret fra millisekunder til sekunder. Det ses, at MySQL var betydelig hurtigere til at håndtere queries på tværs af forskellige tables sammenlignet med vores implementering af MongoDB - hvor det er havde været mere interessant at sammenligne med en single collection strategi. I hvert fald kom en af MongoDBs styrker til udtryk med geospatial data. Selv om arealet var nogenlunde det samme er der stadig en stor usikkerhed i sammenligningen her, da vores Geospatial finder alle byer indenfor en sphere, mens MySQLs implementering brugte et simpelt rektangulært areal. I hvert fald så det, at Mongo var væsentlig hurtigere end MySQL til den sidste query, når et geospatial index anvendes. 

Der er en del afvigelser i forhold til bare at køre queries direkte på databaserne. Blandt andet skal outputtet af querien behandles I java, således at det får samme opbygning - ligegyldigt, hvilken databasen man vælger. Det faktum, at det eksekveres gennem Maven påvirker også resultatet. Alt i alt giver resultatet forholdsvis god mening, at MySQL håndtere relations baseret queries bedst, MongoDBs indbyggede index for geospatial features og Neo4js cypher graph query sprog til håndtering af connected data. Ud fra vores analyse af de implementeringer vi har arbejdet med, så giver det bedst mening at anvende MySQL både pga. hastighed, men også erfaringsmæssige fordel vi har med SQL baserede databaser. Det er muligt, at en bedre implementation af MongoDB kunne have givet god mening, specielt fordi den netop har disse features med geospatial data, hvor det sandsynligt ville være besværligt at skulle udarbejde et lignende system i SQL. Neo4J har også evnen til at bruge Geospatial data, men vi fik det desværre ikke til at virke hos os selvom vi prøvede med en masse queries der gerne skulle have fået det til at fungere.


### Your recommendation, for which database engine to use in such a project for production.

Så hvilken Database ville vi vælge, hvis det skulle i produktion? Svaret er, at det afhænger af, hvad det skal bruges til. Har man behov for relationer, er det muligvis SQL vi ude i - da denne meget etableret database form giver god mening, hvis man skal have en struktur, hvor der er afhængigheder frem og tilbage imellem tables. Vælger man en mere simpel struktur - hvor der eksempelvis kun tages højde for navn og telefonnummer, så give en dokumentbaseret database god mening, da den scaler nemt, i modsætning til den mere komplekse strukturer som man ofte ser i SQL. Graph databaser giver god mening i vores sammenhæng, da graph databaser håndterer many-to-many bedre end relationnelle databaser.  

At opbygge lige netop denne applikation, så gav det god mening at udarbejde den ud fra relationer. Fra Author, til Book, til City - hvor der skabes forbindelse mellem dem i form af hjælpe tabeller (junction tables), så holdes der en forholdsvis simpel struktur. Samtidigt, kunne mongoDB give god mening, hvis man holder det til en enkelt kolletions struktur. MongoDB kan også anbefales, hvis applikationen udvides til at håndtere flere geografiske udregninger. Neo4J kan være en middelvej, da den både kan matche SQLs (hvis ikke slå) hastighed og samtidig provide mulighed for at udnytte geospatial data. Dog er der nogle udfordringer med manglende erfaring, mindre support/udvikling og dårligere scaling for en graph database.    

### Konklusion
Denne database har tydelige relationer, og man kan blive nødt til at gå fra den ene ende til den anden ende i queries. Vi synes sql eller neo4j giver mest mening. 

For os virker det også som om at databaserne lidt er et særtilfælde i det at man nok sjældent ændrer i dem. Jo hurtigere man vil have det, jo mere redundancy kan man lave. Jo oftere man ændrer i sin database, jo besværligere er det at lave om på redundancy. Hvis man sjældent har tænkt sig at opdatere gutenberg-databasen, kan det måske betale sig at lave mere redundancy.

Hele forløbet har være rigtigt stressende for os, da vi har skullet røre noget vi ikke rigtigt har prøvet at arbejde med før og at vi ikke har haft særligt meget tid til at skulle lave det. Her snakker jeg specifikt om BDD, TDD og Neo4j. Det gjorde det heller ikke bedre at vi skulle lave opgaver oven i det her projekt første uge i projekt forløbet. Meget af tiden stod på at få den rigtige data ud til CSV'erne via parseren og derefter importere det i de tre databaser.
Tiden brugt på alt det her opsætning gjorde at vi nærmest ingen tid fik til at lave vores frontend, vi prøvede med en tomcat server frontend med JSP'er. Dette fejlede af en eller anden mystisk årsag, da der ikke gad laves nogen forbindelse til selve serveren, så vi endte med ingen frontend.
