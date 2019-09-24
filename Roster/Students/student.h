#pragma once
#include <string>
#include "degree.h"

using namespace std;


class Student {
	
	public:
		// Constructor
		Student(string studentId,
				string firstName,
				string lastName,
				string email,
				unsigned int age,
				unsigned int courseDays1,
				unsigned int courseDays2,
				unsigned int courseDays3,
				Degree degree);
		
		// Destructor
		~Student();
		
		// Print
		virtual void print() const;
		
		// Accessors
		string getId() const;	
		string getFirstName() const;
		string getLastName() const;
		string getEmail() const;
		unsigned int getAge() const;
		unsigned int getCourseDays1() const;
		unsigned int getCourseDays2() const;
		unsigned int getCourseDays3() const;
		virtual Degree getDegree() const = 0;
		
		// Mutators
		void setId(string id);
		void setFirstName(string name);
		void setLastName(string name);
		void setAge(unsigned int age);
		void setCourseDays1(unsigned int days);
		void setCourseDays2(unsigned int days);
		void setCourseDays3(unsigned int days);
		void setDegree(Degree degree);
	
	// Member data
	private:
		string studentId;
		string firstName;
		string lastName;
		string email;
		unsigned int age;
		unsigned int courseDays[3];
		Degree degree;
};
