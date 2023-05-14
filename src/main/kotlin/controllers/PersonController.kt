package controllers

import annotations.http.EndPoint
import data.Person
import utils.EndPointMethod

class PersonController {
    val persons = mutableListOf<Person.Person>()


    @EndPoint(EndPointMethod.POST, "/person/add")
    fun addPerson(person: Person.Person) {

        println("Person: $person");

        persons.add(person)
    }

    @EndPoint(EndPointMethod.GET, "/person/all")
    fun getAllPersons(): String {
        return persons.toString()
    }


}