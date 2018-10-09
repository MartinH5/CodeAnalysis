import mysql.connector

cnx = mysql.connector.connect(user="root", password="root", database="gutenberg")
cursor = cnx.cursor()

def get_books_mentioning_with_author(cityname):
	query = "SELECT book_t.title, author_t.name FROM abc_relation_t INNER JOIN book_t ON book_t.id = abc_relation_t.book_id INNER JOIN author_t ON author_t.id = abc_relation_t.author_id WHERE city_id = (SELECT city_t.id FROM city_t WHERE city_t.name = ('%s'))" % (cityname)
	cursor.execute(query)
	for entry in cursor:
		print(entry)

def get_cities_in_book(booktitle):
	query = "SELECT city_t.name, city_t.latitude, city_t.longtitude FROM abc_relation_t INNER JOIN book_t ON book_t.id = abc_relation_t.book_id INNER JOIN city_t ON city_t.id = abc_relation_t.city_id WHERE abc_relation_t.book_id = (SELECT id FROM book_t WHERE book_t.title = ('%s'))" % (booktitle)
	cursor.execute(query)
	for entry in cursor:
		print(entry)

def get_cities_mentioned_by_author(authorname):
	query = "SELECT city_t.name, city_t.latitude, city_t.longtitude FROM abc_relation_t JOIN city_t ON city_t.id = abc_relation_t.city_id WHERE abc_relation_t.author_id = (SELECT author_t.id FROM author_t WHERE author_t.name = ('%s'))" % (authorname)
	cursor.execute(query)
	for entry in cursor:
		print(entry)
	
def get_books_mentioning_cities_near(latitude, longtitude, leeway):
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
	

if __name__ == "__main__":
	#get_books_mentioning_with_author("Rio de Janeiro")
	#get_cities_in_book("I Married a Ranger")
	#get_cities_mentioned_by_author("Orr, Lyndon")
	get_books_mentioning_cities_near(31,65,1)
	cnx.close()

