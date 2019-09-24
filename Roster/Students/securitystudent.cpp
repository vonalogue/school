#pragma once
#include <iostream>
#include "securitystudent.h"
using namespace std;


void SecurityStudent::print() const {
	Student::print();
	cout << "SECURITY" << endl;
}

Degree SecurityStudent::getDegree() const {
	return SECURITY;
}