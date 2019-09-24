#pragma once
#include <string>
#include <vector>
#include "student.h"
using namespace std;


class Roster {
	
	public:
		const int ARRAY_SIZE = 5;
		Student* student(int i);				
		void add(string studentId,
				 string firstName,
		   		 string lastName,
				 string email,
				 unsigned int age,
				 unsigned int courseDays1,
				 unsigned int courseDays2,
				 unsigned int courseDays3,
				 Degree degree);
		void remove(string studentId);
		
		// Print functions
		void printAll() const;
		void printCourseDays(string studentId) const;
		void printInvalidEmails() const;
		void printByDegree(Degree degree) const;

		//Destructor
		~Roster();

	private:
		Student* rosterArray[5] = {nullptr, nullptr, nullptr, nullptr, nullptr};
};