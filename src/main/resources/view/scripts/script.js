"use strict";

var backendAddress = "http://localhost:8080";

var request = function (address, id) {
        var self = ko.observableArray();

        self.url = address;
        self.postUrl = self.url;

        self.get = function (query) {
            if (self.sub) {
                self.sub.dispose();
            }

            var url = self.url;

            if (query)
                url = url + query;

            $.ajax({
                dataType: "json",
                url: url,
                success: function (data) {
                    self.removeAll();

                    if (!Array.isArray(data)) {
                        var tempData = data;
                        data = [];
                        data.push(tempData);
                    }

                    data.forEach(function (element) {
                        var object = ko.mapping.fromJS(element, {ignore: ["link"]});
                        object.links = [];

                        if ($.isArray(element.links)) {
                            element.links.forEach(function (link) {
                                object.links[link.params.rel] = link.href;
                            });
                        } else {
                            object.links[element.links.params.rel] = element.links.href;
                        }

                        self.push(object);

                        ko.computed(function () {
                            return ko.toJSON(object);
                        }).subscribe(function () {
                            self.updateRequest(object);
                        });
                    });

                    self.sub = self.subscribe(function (changes) {
                        changes.forEach(function (change) {
                            if (change.status === 'added') {
                                self.saveRequest(change.value);
                            }
                            if (change.status === 'deleted') {
                                self.deleteRequest(change.value);
                            }
                        });
                    }, null, "arrayChange");
                }
            });
        };

        self.saveRequest = function (object) {
            $.ajax({
                url: self.postUrl,
                dataType: "json",
                contentType: "application/json",
                data: ko.mapping.toJSON(object),
                method: "POST",
                success: function (data) {
                    var response = ko.mapping.fromJS(data);
                    object[id](response[id]());

                    object.links = [];

                    if ($.isArray(data.links)) {
                        data.links.forEach(function (link) {
                            object.links[link.params.rel] = link.href;
                        });
                    } else {
                        object.links[data.links.params.rel] = data.links.href;
                    }

                    ko.computed(function () {
                        return ko.toJSON(object);
                    }).subscribe(function () {
                        self.updateRequest(object);
                    });
                }
            });
        };

        self.updateRequest = function (object) {
            if (object['course'] != null) {
                object.course = ko.utils.arrayFirst(viewModel.courses(), function (course) {
                    if (object.course.id() === course.id()) {
                        return course;
                    }
                });
            }
            if (object['course'] != null) {
                $.ajax({
                    url: backendAddress + object.links['self'],
                    dataType: "json",
                    contentType: "application/json",
                    data: ko.mapping.toJSON(object, {ignore: ["links"]}),
                    method: "PUT"
                });
            }
        };

        self.deleteRequest = function (object) {
            $.ajax({
                url: backendAddress + object.links['self'],
                method: "DELETE"
            });
        };

        self.add = function (form) {
            var data = {};
            $(form).serializeArray().map(function (x) {
                data[x.name] = x.value;
            });

            data[id] = null;
            self.push(ko.mapping.fromJS(data));

            $(form).each(function () {
                this.reset();
            });

        };

        self.delete = function () {
            self.remove(this);
            self.deleteRequest(this);
        };

        self.parseQuery = function () {
            self.get('?' + $.param(ko.mapping.toJS(self.queryParams)));
        };

        return self;
    }
;

function viewModel() {
    var self = this;

    self.students = new request(backendAddress + "/students", "index");
    self.students.getGrades = function () {
        window.location = "#grades";
        self.grades.selectedStudent(this.index());
        self.grades.selectedCourse(null);
        self.grades.url = backendAddress + "/students/" + this.index() + "/grades";
        self.grades.get();
    };
    self.students.queryParams = {
        firstName: ko.observable(),
        lastName: ko.observable(),
        birthDate: ko.observable(),
        dateRelation: ko.observable()
    };

    self.students.getIndex = ko.observable();
    self.indexTrigger = ko.computed(function () {
        if (self.students.getIndex() != undefined) {
            self.students.get('/' + self.students.getIndex());
        }
    }, self);

    self.students.getRelG = ko.observable();
    self.relGTrigger = ko.computed(function () {
        if (self.students.getRelG() !== undefined) {
            self.students.getRelL(false);

            if (self.students.getRelG() === true)
                self.students.queryParams.dateRelation("after");
            else
                self.students.queryParams.dateRelation("equal");
        }
        else
            self.students.queryParams.dateRelation("equal");
    }, self);

    self.students.getRelL = ko.observable();
    self.relLTrigger = ko.computed(function () {
        if (self.students.getRelL() !== undefined) {
            self.students.getRelG(false);

            if (self.students.getRelL() === true)
                self.students.queryParams.dateRelation("before");
            else
                self.students.queryParams.dateRelation("equal");
        }
        else
            self.students.queryParams.dateRelation("equal");
    }, self);

    Object.keys(self.students.queryParams).forEach(function (key) {
        self.students.queryParams[key].subscribe(function () {
            self.students.parseQuery();
        });
    });
    self.students.get();

    self.courses = new request(backendAddress + "/courses", "id");
    self.courses.queryParams = {
        name: ko.observable(),
        lecturer: ko.observable()
    };

    Object.keys(self.courses.queryParams).forEach(function (key) {
        self.courses.queryParams[key].subscribe(function () {
            self.courses.parseQuery();
        });
    });
    self.courses.get();

    self.grades = new request(backendAddress + "/grades", "id");
    self.grades.selectedCourse = ko.observable();
    self.grades.selectedStudent = ko.observable();

    self.grades.add = function (form) {
        self.grades.postUrl = backendAddress + '/students/' + self.grades.selectedStudent() + '/grades';
        var data = {};
        $(form).serializeArray().map(function (x) {
            data[x.name] = x.value;
        });

        console.log(self.grades.selectedCourse());
        data.course = ko.utils.arrayFirst(self.courses(), function (course) {
            if (course.id() === self.grades.selectedCourse()) {
                return ko.mapping.toJS(course);
            }
        });

        data.id = self.grades.selectedCourse();
        self.grades.push(ko.mapping.fromJS(data));
        $(form).each(function () {
            this.reset();
        });
    };

    self.grades.queryParams = {
        value: ko.observable(),
        courseName: ko.observable(),
        date: ko.observable()
    };

    Object.keys(self.grades.queryParams).forEach(function (key) {
        self.grades.url = backendAddress + '/students/' + self.grades.selectedStudent() + "/grades",
            self.grades.queryParams[key].subscribe(function () {
                self.grades.parseQuery();
            });
    });
}

var viewModel = new viewModel();

$(document).ready(function () {
    ko.applyBindings(viewModel);
});