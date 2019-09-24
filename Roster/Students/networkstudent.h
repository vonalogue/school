#pragma once
#include "student.h"


class NetworkStudent : public Student {
public:
	using Student::Student;
	virtual void print() const;
	virtual Degree getDegree() const;
};
