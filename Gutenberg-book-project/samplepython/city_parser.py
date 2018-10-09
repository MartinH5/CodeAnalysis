import pandas as pd
import mysql.connector

cities_pop = {}

cities_lookup = {}
cities_full = set()

def parse_cities_sql():
	data = pd.read_csv("cities15000.txt", sep="\t")
	for row in data.itertuples():
		if row[3] in cities_pop:
			if row[15] > cities_pop[row[3]]["pop"]:
				cities_pop[row[3]]["pop"] = int(row[15])
				cities_pop[row[3]]["lat"] = int(row[5])
				cities_pop[row[3]]["long"] = int(row[6])
				continue
		cities_pop[row[3]] = {}
		cities_pop[row[3]]["pop"] = int(row[15])
		cities_pop[row[3]]["lat"] = float(row[5])
		cities_pop[row[3]]["long"] = float(row[6])

def parse_cities_python():
	data = pd.read_csv("cities15000.txt", sep="\t")
	for row in data.itertuples():
		if " " in row[3]:
			cities_lookup[row[3].split()[0]] = "1"
			cities_full.add(row[3])
			continue
		if row[3] in cities_lookup:
			cities_lookup[row[3]] = 1
			continue
		cities_lookup[row[3]] = 0
		cities_full.add(row[3])
	#manually print these to stdout and redirect to file
	#print(cities_full)
	#print(cities_lookup)

def import_cities_sql():
	cnx = mysql.connector.connect(user="root", password="root", database="gutenberg")
	cursor = cnx.cursor()

	query = "INSERT INTO city_t (name, latitude, longtitude) VALUES (%s, %s, %s)"
	for city in cities_pop:
		latitude = cities_pop[city]["lat"]
		longtitude = cities_pop[city]["long"]
		try:
			cursor.execute(query, (city, str(latitude), str(longtitude)))
		except mysql.connector.Error as err:
			print("Whoops: {}".format(err))
	cnx.commit()
	cnx.close()

if __name__ == "__main__":
	parse_cities_python()

