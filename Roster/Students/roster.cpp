#pragma once
#include <iostream>
#include <sstream>
#include <regex>
#include "roster.h"
#include "securitystudent.h"
#include "networkstudent.h"
#include "softwarestudent.h"
using namespace std;


void Roster::add(string studentId,
	string firstName,
	string lastName,
	string email,
	unsigned int age,
	unsigned int courseDays1,
	unsigned int courseDays2,
	unsigned int courseDays3,
	Degree degree)
{
	// Replace the first null pointer found with a pointer to a Student object.
	for (int i = 0; i < this->ARRAY_SIZE; ++i) {
		if (this->rosterArray[i] == nullptr) {
			switch (degree) {
			case SECURITY:
				this->rosterArray[i] =
					new SecurityStudent(
						studentId,
						firstName,
						lastName,
						email,
						age,
						courseDays1,
						courseDays2,
						courseDays3,
						SECURITY);
				return;
			case NETWORK:
				this->rosterArray[i] =
					new NetworkStudent(
						studentId,
						firstName,
						lastName,
						email,
						age,
						courseDays1,
						courseDays2,
						courseDays3,
						NETWORK);
				return;
			case SOFTWARE:
				this->rosterArray[i] =
					new SoftwareStudent(
						studentId,
						firstName,
						lastName,
						email,
						age,
						courseDays1,
						courseDays2,
						courseDays3,
						SOFTWARE);
				return;
			}
		}
	}
	cout << "CANNOT ADD STUDENT; MAX CAPACITY REACHED." << endl;
}


Student* Roster::student(int i) {
	return this->rosterArray[i];
}


void Roster::remove(string studentId) {
	for (int i = 0; i < this->ARRAY_SIZE; ++i) {
		if (this->rosterArray[i] != nullptr && this->rosterArray[i]->getId() == studentId) {
			delete this->rosterArray[i];
			this->rosterArray[i] = nullptr;
			return;
		}
	}
	cout << "ID NOT FOUND." << endl;
}


void Roster::printAll() const {
	for (int i = 0; i < this->ARRAY_SIZE; ++i) {
		if (this->rosterArray[i] != nullptr)
			this->rosterArray[i]->print();
	}
	cout << endl;
}


void Roster::printCourseDays(string studentId) const {
	for (int i = 0; i < this->ARRAY_SIZE; ++i) {
		if (this->rosterArray[i] != nullptr && this->rosterArray[i]->getId() == studentId) {
			int avg = (this->rosterArray[i]->getCourseDays1() +
				this->rosterArray[i]->getCourseDays2() +
				this->rosterArray[i]->getCourseDays3()) / 3;
				cout << avg << endl << endl;
			return;
		}
	}
	cout << "ID NOT FOUND" << endl << endl;
}


void Roster::printInvalidEmails() const {
	regex validFormat("[\\w\\S]+@[\\w\\S]+[.]\\w+");
	cout << "INVALID EMAILS: " << endl;
	for (int i = 0; i < this->ARRAY_SIZE; ++i) {
		if (this->rosterArray[i] != nullptr && !regex_match(this->rosterArray[i]->getEmail(), validFormat))
			cout << this->rosterArray[i]->getEmail() << endl;
	}
	cout << endl;
}


void Roster::printByDegree(Degree degree) const {
	for (int i = 0; i < this->ARRAY_SIZE; ++i) {
		if (this->rosterArray[i] != nullptr && this->rosterArray[i]->getDegree() == degree)
			this->rosterArray[i]->print();
	}
	cout << endl;
}


Roster::~Roster() {
	cout << "Roster destroyed." << endl;
}


void main() {
	const int dataSize = 4;
	const string studentData[dataSize] =
		{ "A1,John,Smith,John1989@gm ail.com,20,30,35,40,SECURITY",
		 "A2,Suzan,Erickson,Erickson_1990@gmailcom,19,50,30,40,NETWORK",
		 "A3,Jack,Napoli,The_lawyer99yahoo.com,19,20,40,33,SOFTWARE",
		 "A4,Erin,Black,Erin.black@comcast.net,22,50,58,40,SECURITY" };
	
	vector<string> studentDataTokens;						// Temporarily store tokens for each line read from studentData.
	istringstream* inSS;
	string dataLine;
	string token;
	string degreeStr;
	Degree degree;
	Roster classRoster;

	cout << "Course: C867 | Language: C++ | Student ID: 000628650 | Name: Dennis Golanov" << endl << endl;

	// Build roster with student data.
	for (int i = 0; i < dataSize; ++i) {
		dataLine = studentData[i];
		inSS = new istringstream(dataLine);

		while (getline(*inSS, token, ',')) {
			studentDataTokens.push_back(token);
		}

		degreeStr = studentDataTokens.at(8);
		if (degreeStr == "SECURITY") {
			degree = SECURITY;
		} else if (degreeStr == "NETWORK") {
			degree = NETWORK;
		} else if (degreeStr == "SOFTWARE") {
			degree = SOFTWARE;
		}

		classRoster.add(studentDataTokens.at(0),
			studentDataTokens.at(1),
			studentDataTokens.at(2),
			studentDataTokens.at(3),
			stoi(studentDataTokens.at(4)),
			stoi(studentDataTokens.at(5)),
			stoi(studentDataTokens.at(6)),
			stoi(studentDataTokens.at(7)),
			degree);

		delete inSS;
		studentDataTokens.clear();
	}

	classRoster.add("A9", "Dennis", "Golanov", "dgolano@wgu.edu", 27, 25, 15, 15, SOFTWARE);	
	classRoster.printAll();
	classRoster.printInvalidEmails();

	for (int i = 0; i < classRoster.ARRAY_SIZE; ++i) {
		classRoster.printCourseDays(classRoster.student(i)->getId());
	}

	classRoster.printByDegree(SOFTWARE);
	classRoster.printByDegree(SECURITY);
	classRoster.remove("A3");	
	classRoster.remove("A3");

}