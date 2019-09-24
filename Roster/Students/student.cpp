#pragma once
#include <iostream>
#include <string>
#include "student.h"
using namespace std;


// Constructor
Student::Student(string studentId, 
				 string firstName,
				 string lastName,
				 string email,
				 unsigned int age,
				 unsigned int courseDays1,
				 unsigned int courseDays2,
				 unsigned int courseDays3,
				 Degree degree)
{
	this->studentId = studentId;
	this->firstName = firstName;
	this->lastName = lastName;
	this->email = email;
	this->age = age;
	this->courseDays[0] = courseDays1;
	this->courseDays[1] = courseDays2;
	this->courseDays[2] = courseDays3;
	this->degree = degree;
}

// Destructor
Student::~Student() { 
	cout << "Student " << this->studentId << " deleted." << endl;
}

// Print
void Student::print() const {
	cout << this->studentId   	<< '\t'
		 << this->firstName   	<< ' '
		 << this->lastName    	<< '\t'
		 << this->email 	  	<< '\t'
		 <<	this->age		  	<< "\t{";
	
	for (int i = 0; i < 3; i++) {
		cout << this->courseDays[i];
		if (i < 2)
			cout << ", ";
	}
	cout << "}\t";
}


// Accessors
string Student::getId() const					{ return this->studentId;  		}
string Student::getFirstName() const			{ return this->firstName;  		}
string Student::getLastName() const				{ return this->lastName;   		}
string Student::getEmail() const				{ return this->email;	   		}
unsigned int Student::getAge()	const			{ return this->age;		   		}
unsigned int Student::getCourseDays1() const    { return this->courseDays[0]; 	}
unsigned int Student::getCourseDays2() const    { return this->courseDays[1]; 	}
unsigned int Student::getCourseDays3() const    { return this->courseDays[2]; 	}


// Mutators
void Student::setId(string id) 					{ this->studentId = id; 		}
void Student::setFirstName(string name)			{ this->firstName = name; 		}
void Student::setLastName(string name)			{ this->lastName  = name;		}
void Student::setAge(unsigned int age)			{ this->age = age; 				}
void Student::setCourseDays1(unsigned int days) { this->courseDays[0] = days;	}
void Student::setCourseDays2(unsigned int days)	{ this->courseDays[1] = days; 	}
void Student::setCourseDays3(unsigned int days)	{ this->courseDays[2] = days; 	}
void Student::setDegree(Degree degree)			{ this->degree = degree;		}













