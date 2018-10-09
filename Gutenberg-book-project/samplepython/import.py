# -*- coding: utf-8 -*-
import pandas as pd
import mysql.connector
import sys
import rdflib
import os.path

# idiot: 13329 rows in set (0.02 sec)

counter = 0

from allbooks import allbooks

def book_exists(idx):
	if "/home/lasse/books/" + idx + ".txt" in allbooks:
		return True
	if "/home/lasse/books/" + idx + "/" + idx + ".txt" in allbooks:
		return True
	return False

def import_authors_books():
	global counter

	cnx = mysql.connector.connect(user="root", password="root", database="gutenberg")
	cursor = cnx.cursor()
	with open("pub.txt", "r") as myfile:
		for line in myfile:
			import_author_book_single(line.replace("\n", ""), cnx, cursor)
			counter += 1
			print("{}".format(counter))
	cnx.close()

def print_city_ids():
	cnx = mysql.connector.connect(user="root", password="root", database="gutenberg")
	cursor = cnx.cursor()
	query = "SELECT * FROM city_t"
	cursor.execute(query)
	for cityid, name, latitude, longtitude in cursor:
		print("\"{}\": {}".format(name, cityid))
	cnx.close()


def import_author_book_single(myidx, cnx, cursor):
	if not book_exists(myidx):
		return
	g = rdflib.Graph()
	result = g.parse("/home/lasse/meta/cache/epub/" + myidx + "/pg" + myidx + ".rdf")
	for s,p,o in g:
		if "pgterms/name" in p:
			query = "INSERT INTO author_t (name) VALUES ('%s')" % (o.encode("utf-8").strip().replace("'",""))
			try:
				cursor.execute(query)
				cnx.commit()
			except mysql.connector.Error as err:
				print("Whoops: {}".format(err))
		if "dc/terms/title" in p:
			query = "INSERT INTO book_t (title) VALUES ('%s')" % (o.encode("utf-8").strip().replace("'",""))
			try:
				cursor.execute(query)
				cnx.commit()
			except mysql.connector.Error as err:
				print("Whoops: {}".format(err))
	return

def create_author_book_relations():
	global counter

	cnx = mysql.connector.connect(user="root", password="root", database="gutenberg")
	cursor = cnx.cursor()
	author_ids = {}
	query = "SELECT * FROM author_t"
	cursor.execute(query)
	for author in cursor:
		author_ids[author[1].encode("utf-8")] = author[0]
	with open("pub.txt", "r") as myfile:
		for line in myfile:
			authormatches = set()
			this_book_id = -1
			myidx = line.replace("\n", "")	
			if not book_exists(myidx):
				continue
			g = rdflib.Graph()
			result = g.parse("/home/lasse/meta/cache/epub/" + myidx + "/pg" + myidx + ".rdf")
			for s,p,o in g:
				if "dc/terms/title" in p:
					query = "SELECT * FROM book_t WHERE title=('%s')" % (o.encode("utf-8").strip().replace("'",""))
					cursor.execute(query)
					for result in cursor:
						this_book_id = result[0]
						# assume no collisions
				if "pgterms/name" in p:
					authormatches.add(o.encode("utf-8").strip().replace("'",""))
			# jesus christ
			#print("bookid is {}".format(this_book_id))
			for author in authormatches:
				#print("{} {}".format(author, author_ids[author]))
				insertquery = "INSERT INTO author_book_relation_t (author_id, book_id) VALUES (%s, %s)" % (author_ids[author], this_book_id)
				try:
					cursor.execute(insertquery)
					cnx.commit()
				except mysql.connector.Error as err:
					print("Whoops: {}".format(err))
			print("-------")
			counter += 1
			print("{}".format(counter))

	cnx.close()



if __name__ == "__main__":
	options = {
		"import_authors_books":		import_authors_books,
		#"print_author_book_relations":	print_author_book_relations
	}

	print_city_ids();
	#create_author_book_relations()

