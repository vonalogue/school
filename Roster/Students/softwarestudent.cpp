#pragma once
#include <iostream>
#include "softwarestudent.h"
using namespace std;


void SoftwareStudent::print() const {
	Student::print();
	cout << "SOFTWARE" << endl;
}

Degree SoftwareStudent::getDegree() const {
	return SOFTWARE;
}