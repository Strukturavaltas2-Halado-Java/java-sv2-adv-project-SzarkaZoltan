# java-sv2-adv-project-SzarkaZoltan
# Beer-Webshop Vizsgaremek

## Leírás


Az alapötlet egy olyan alkalmazás elkészítése volt amiben létre lehet hozni söröket árusító webshopokat, söröket és a hozzájuk tartozó összetevőket.
---

## Felépítés

### Entitás 1

A `Webshop` entitás a következő attribútumokkal rendelkezik:

* id
* name (nem lehet üres)
* e-mailAddress (meg kell felelnie az e-mail címhez tartozó szabályoknak)
* beers (sörök listája)


Végpontok: 

| HTTP metódus | Végpont                 | Leírás                                                                 |
| ------------ | ----------------------- | ---------------------------------------------------------------------- |
| GET          | `"/api/webshops"`        | lekérdezi az összes webshop-ot                                         |
| POST          | `"/api/webshops"`   | létrehoz egy új webshopot                                      |
| PUT          | `"/api/webshops/{id}/beers"`        | hozzárendel egy sört `id` alapján egy webshop-hoz                                         |
| PUT          | `"/api/webshops/{id}/beers/{beerId}"`   | levesz egy sört `beerId` alapján egy webshop kínálatából                                      |
| DELETE          | `"/api/webshops/{id}"`        | töröl egy webshop-ot `id` alapján                                         |




---

### Entitás 2 

A `Beer ` entitás a következő attribútumokkal rendelkezik:

* id
* name (nem lehet üres)
* brand
* type
* price (minimum értéke 500 maximuma 10 000 lehet)
* alcohol (nem lehet negatív az értéke és nem lehet több mint 0.8)
* ingredients (összetevők listája)


A `Webshop` és a `Beer` entitások között kétirányú, n-n kapcsolat van.

Végpontok:

| HTTP metódus | Végpont                 | Leírás                                                                 |
| ------------ | ----------------------- | ---------------------------------------------------------------------- |
| GET          | `"/api/beers"`        | lekérdezi az összes sört                                         |
| GET          | `"/api/beers/{id}"`   | lekérdez egy sört `id` alapján                                      |
| GET          | `"/api/beers/brands"`        | lekérdezi a söröknek a márkáit                                         |
| GET          | `"/api/beers/{id}/ingredients"`   | lekérdez egy sörhöz tartozó összes összetevőt listában                                      |
| POST          | `"/api/beers"`        | létrehoz egy új sört                                         |
| POST          | `"/api/beers/{id}/ingredients"`   | hozzárendel egy sörhöz egy új összetevőt                                      |
| PUT          | `"/api/beers/{id}/webshops"`        | hozzárendel egy webshop-ot `id` alapján egy sörhöz                                         |
| DELETE          | `"/api/beers/{id}"`   | töröl egy sört `id` alapján                                      |

### Embeddable

A `Ingredient` embeddable a következő attribútumokkal rendelkezik:

* id (a sörhöz tartozó elsődleges kulcs mint idegen kulcs)
* name (nem lehet üres)
* ratio (nem lehet negatív és nem lehet nagyobb 0.3-nál)

Egy összetevő csak egyszer szerepelhet egy sörnél.

---

## Technológiai részletek


A program egy háromrétegű alkalmazás Java programozási nyelven megírva Spring Boot keretrendszerben.
Az alkalmazás gitet használ verziókezelésre és gitActions-t a continous integration-höz. Utóbbi felel a megfelelő kódminőség biztosításáért amit unit tesztek futtatásával és statikus elemzéssel ér el.
Az adatok mentéséhez mariadb relációs adatbázist használtam.
Az adatbázis migrálásért Flyway felel. Ő hozza létre a szükséges táblákat. Az adatbázis eléréséhez szükséges adatok az application.properties-be vannak kiszervezve.
Készültek webtestclient integrációs tesztek.
A beviteli mezők ellenőrzésére validációkat használtam.
Mind az adatbázis mind az alkalmazás futtatható docker konténerből.
Swagger-UI használata a végpontok tesztelésére.

---
