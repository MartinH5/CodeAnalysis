import mysql.connector

def get_books_mentioning_with_author(cityname):
	cnx = mysql.connector.connect(user="root", password="root", database="gutenberg")
	cursor = cnx.cursor()

	author_book_rels = set()

	author_book_rels_query = "SELECT * FROM author_book_relation_t WHERE author_book_relation_t.book_id IN (SELECT book_id from book_city_relation_t WHERE book_city_relation_t.city_id IN (SELECT city_t.id FROM city_t WHERE city_t.name = ('%s')))" % (cityname)
	cursor.execute(author_book_rels_query)
	
	for entry in cursor:
		author_book_rels.add(entry)
	
	res = set()
	for rel in author_book_rels:
		query = "SELECT book_t.title, author_t.name FROM author_book_relation_t INNER JOIN author_t ON author_t.id = author_book_relation_t.author_id INNER JOIN book_t ON book_t.id = author_book_relation_t.book_id WHERE author_book_relation_t.id = (%s)" % (rel[0])
		cursor.execute(query)
		for entry in cursor:
			print(entry)

	cnx.close()

def get_cities_in_book(booktitle):
	cnx = mysql.connector.connect(user="root", password="root", database="gutenberg")
	cursor = cnx.cursor()

	query = "SELECT * FROM book_city_relation_t INNER JOIN city_t ON book_city_relation_t.city_id = city_t.id WHERE book_city_relation_t.book_id IN (SELECT book_t.id FROM book_t WHERE book_t.title = ('%s'))" % (booktitle)
	cursor.execute(query)

	for entry in cursor:
		print(entry)
	cnx.close()

def get_cities_mentioned_by_author(authorname):
	cnx = mysql.connector.connect(user="root", password="root", database="gutenberg")
	cursor = cnx.cursor()

	ab_rels = set()

	author_id = -1
	query = "SELECT * FROM author_t WHERE author_t.name = ('%s')" % (authorname)
	cursor.execute(query)
	for entry in cursor:
		author_id = entry[0]
	
	query = "SELECT book_t.id, book_t.title FROM author_book_relation_t INNER JOIN book_t ON author_book_relation_t.book_id = book_t.id WHERE author_book_relation_t.author_id = (%s)" % (author_id)
	cursor.execute(query)
	for entry in cursor:
		ab_rels.add(entry)

	for each in ab_rels:
		query = "SELECT book_t.title, city_t.name, city_t.latitude, city_t.longtitude FROM book_city_relation_t INNER JOIN book_t ON book_t.id = book_city_relation_t.book_id INNER JOIN city_t ON city_t.id = book_city_relation_t.city_id WHERE book_t.id = (%s)" % (each[0])
		cursor.execute(query)
		for match in cursor:
			print(match)
	
	cnx.close()
	
def get_books_mentioning_cities_near(latitude, longtitude, leeway):
	cnx = mysql.connector.connect(user="root", password="root", database="gutenberg")
	cursor = cnx.cursor()

	found_cities = set()

	query = "SELECT * FROM city_t WHERE latitude > (%s) AND latitude < (%s) AND longtitude > (%s) AND longtitude < (%s)" % (latitude - leeway, latitude + leeway, longtitude - leeway, longtitude + leeway)
	cursor.execute(query)
	for city in cursor:
		found_cities.add(city)

	for city in found_cities:
		print("BOOKS THAT MENTION {}".format(city[1]))
		query = "SELECT * FROM book_city_relation_t JOIN book_t ON book_t.id = book_city_relation_t.book_id WHERE city_id = (%s)" % (city[0])
		cursor.execute(query)
		for book in cursor:
			print(book)
	
	cnx.close()


if __name__ == "__main__":
	#get_books_mentioning_with_author("Rio de Janeiro")
	#get_cities_in_book("I Married a Ranger")
	#get_cities_mentioned_by_author("Orr, Lyndon")
	get_books_mentioning_cities_near(31,65,1)

