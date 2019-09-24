#pragma once
#include <iostream>
#include "networkstudent.h"
using namespace std;


void NetworkStudent::print() const {
	Student::print();
	cout << "NETWORK" << endl;
}

Degree NetworkStudent::getDegree() const {
	return NETWORK;
}