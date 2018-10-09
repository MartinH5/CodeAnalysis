from data_cities_full import cities_full
from data_cities_lookup import cities_maybe
from data_cities_id import cities_ids
import os.path
import rdflib
import mysql.connector

found_cities = set()
found_multiword = set()

delete_this = {".", ",", "?", "\"", "'", "!", "_", ")", "("}
counter = 0

def find_multiword(wordlist, idx):
	substring = ""
	longest_possible = ""
	longestmatch = 0
	maxlen = idx + 6
	if maxlen > len(wordlist):
		maxlen = len(wordlist)
	for x in range(idx, maxlen):
		substring += wordlist[x]
		if substring in cities_full:
			longestmatch = x
			longest_possible = substring
		if x < maxlen - 1:
			substring += " "
	if longest_possible == "":
		return 0
	found_multiword.add(longest_possible)
	return len(longest_possible.split())


def find_cities_chunk(chunk):
	for x in delete_this:
		chunk = chunk.replace(x, "")
	words = chunk.split()
	for x in range(0, len(words)):
		if words[x] in cities_maybe:
			if cities_maybe[words[x]] == 0:
				found_cities.add(words[x])
				continue
			x += find_multiword(words, x)

def find_cities_file(filename):
	with open(filename, "r") as myfile:
		chunk = ""
		for line in myfile:
			if line == "\r\n":
				find_cities_chunk(chunk)
				chunk = ""
				continue
			chunk += line.strip() + " "

def get_book_sql_id(idx):
	g = rdflib.Graph()
	result = g.parse("/home/lasse/meta/cache/epub/" + myidx + "/pg" + myidx + ".rdf")
	this_book_title = ""
	for s,p,o in g:
		if "dc/terms/title" in p:
			this_book_title = o.encode("utf-8").strip().replace("'","")
			print("Found title: {}".format(this_book_title))
			break

	if this_book_title == "":
		return -1
	
	cnx = mysql.connector.connect(user="root", password="root", database="gutenberg")
	cursor = cnx.cursor()
	query = "SELECT * FROM book_t WHERE title=('%s')" % (this_book_title)
	cursor.execute(query)
	for book in cursor:
		return int(book[0])
		cnx.close()
	return -1

def make_city_relations(idx):
	myfile = "/home/lasse/books/" + idx + ".txt"
	if not os.path.isfile(myfile):
		myfile = "/home/lasse/books/" + idx + "/" + idx + ".txt"
		if not os.path.isfile(myfile):
			return
	
	this_book_id = get_book_sql_id(idx)
	if this_book_id == -1:
		return

	find_cities_file(myfile)

	cnx = mysql.connector.connect(user="root", password="root", database="gutenberg")
	cursor = cnx.cursor()

	# single word cities
	for city in found_cities:
		query = "INSERT INTO book_city_relation_t (book_id, city_id) VALUES (%s, %s)" % (this_book_id, cities_ids[city])
		try:
			cursor.execute(query)
			cnx.commit()
		except mysql.connector.Error as err:
			print("Whoops: {}".format(err))
	
	# multi word cities
	for city in found_multiword:
		query = "INSERT INTO book_city_relation_t (book_id, city_id) VALUES (%s, %s)" % (this_book_id, cities_ids[city])
		try:
			cursor.execute(query)
			cnx.commit()
		except mysql.connector.Error as err:
			print("Whoops: {}".format(err))
	found_cities.clear()
	found_multiword.clear()
	cnx.close()


if __name__ == "__main__":
	with open("pub.txt", "r") as pubfile:
		for line in pubfile:
			myidx = line.replace("\n", "")
			make_city_relations(myidx)
			counter += 1
			print("{}".format(counter))
	#with open("/home/lasse/books/10705.txt", "r") as myfile:
	#print("--------")
	#print(found_cities)
	#print("--------")
	#print(found_multiword)

