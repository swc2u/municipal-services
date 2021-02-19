"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.getAdditionalPensionPercentageForFamily = exports.isEldestDependent = exports.getAgeOnRetirementDate = exports.getAge = exports.getDOJ = exports.getPensionConfigurationValue = exports.getAdditionalPensionPercentage = exports.getIRPercentage = exports.getCommutationMultiplier = exports.getCommutationPercentage = exports.getDAPercentage = exports.lastDayOfMonth = exports.isDateLastDayOfMonth = exports.getMonthsDaysInRange = exports.getGQSDay = exports.getGQSMonth = exports.getGQSYear = exports.getGQS = exports.getNQS = exports.getNQSDay = exports.getNQSMonth = exports.getNQSYear = exports.getHalfYearOfService = exports.getYearOfService = undefined;

var _uniqBy = require("lodash/uniqBy");

var _uniqBy2 = _interopRequireDefault(_uniqBy);

var _uniq = require("lodash/uniq");

var _uniq2 = _interopRequireDefault(_uniq);

var _get = require("lodash/get");

var _get2 = _interopRequireDefault(_get);

var _filter = require("lodash/filter");

var _filter2 = _interopRequireDefault(_filter);

var _findIndex = require("lodash/findIndex");

var _findIndex2 = _interopRequireDefault(_findIndex);

var _isEmpty = require("lodash/isEmpty");

var _isEmpty2 = _interopRequireDefault(_isEmpty);

var _lte = require("lodash/lte");

var _lte2 = _interopRequireDefault(_lte);

var _gte = require("lodash/gte");

var _gte2 = _interopRequireDefault(_gte);

var _orderBy = require("lodash/orderBy");

var _orderBy2 = _interopRequireDefault(_orderBy);

var _api = require("./api");

var _envVariables = require("../envVariables");

var _envVariables2 = _interopRequireDefault(_envVariables);

var _linq = require("linq");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var math = require("mathjs");
var DateDiff = require("date-diff");

var getYearOfService = exports.getYearOfService = function getYearOfService(employee) {
  var yearsOfService = getNQSYear(employee);
  return yearsOfService;
};

var getHalfYearOfService = exports.getHalfYearOfService = function getHalfYearOfService(employee) {
  var halfYearsOfService = getNQSYear(employee) * 2;
  var nqsMonth = getNQSMonth(employee);
  if (nqsMonth >= 3) {
    halfYearsOfService += 1;
    if (nqsMonth >= 9) {
      halfYearsOfService += 1;
    };
  };

  return halfYearsOfService;
};

var getNQSYear = exports.getNQSYear = function getNQSYear(employee) {
  var nqsYear = 0;

  var nqs = getNQS(employee);
  if (nqs) {
    nqsYear = Number(nqs.split("|")[0]);
  }

  return nqsYear;
};

var getNQSMonth = exports.getNQSMonth = function getNQSMonth(employee) {
  var nqsMonths = 0;

  var nqs = getNQS(employee);
  if (nqs) {
    nqsMonths = Number(nqs.split("|")[1]);
  }

  return nqsMonths;
};

var getNQSDay = exports.getNQSDay = function getNQSDay(employee) {
  var nqsDay = 0;

  var nqs = getNQS(employee);
  if (nqs) {
    nqsDay = Number(nqs.split("|")[2]);
  }

  return nqsDay;
};

//Net Qualifying Service as Years|Months|Days
var getNQS = exports.getNQS = function getNQS(employee) {
  var nqs = "";

  var gqs = getGQS(employee);
  var gqsDays = Number(gqs.split("|")[2]);
  var gqsMonths = Number(gqs.split("|")[1]);
  var gqsYears = Number(gqs.split("|")[0]);
  var days = gqsDays;
  var months = gqsMonths;
  var years = gqsYears;

  var totalNoPayLeavesDays = employee.totalNoPayLeavesDays ? Number(employee.totalNoPayLeavesDays) : 0;
  var totalNoPayLeavesMonths = employee.totalNoPayLeavesMonths ? Number(employee.totalNoPayLeavesMonths) : 0;
  var totalNoPayLeavesYears = employee.totalNoPayLeavesYears ? Number(employee.totalNoPayLeavesYears) : 0;

  if (days >= totalNoPayLeavesDays) {
    days = days - totalNoPayLeavesDays;
  } else {
    if (months > 0) {
      days = days + 30 - totalNoPayLeavesDays;
      months = months - 1;
    } else {
      years = years - 1;
      months = months + 12;
      days = days + 30 - totalNoPayLeavesDays;
      months = months - 1;
    }
  }
  if (months >= totalNoPayLeavesMonths) {
    months = months - totalNoPayLeavesMonths;
  } else {
    months = months + 12 - totalNoPayLeavesMonths;
    years = years - 1;
  }
  years = years - totalNoPayLeavesYears;

  //years=Math.trunc(months/12) ;
  //months -=(years*12);
  nqs = years + "|" + months + "|" + days;

  return nqs;
};

//Gross Qualifying Service as Years|Months|Days
var getGQS = exports.getGQS = function getGQS(employee) {
  var gqs = "";
  var years = 0;
  var months = 0;
  var days = 0;

  var serviceHistory = employee.serviceHistory[0];
  var dtStart = new Date(serviceHistory.serviceFrom);
  dtStart = new Date(dtStart.getFullYear(), dtStart.getMonth(), dtStart.getDate() + 1);
  var dtEnd = new Date(serviceHistory.serviceTo ? serviceHistory.serviceTo : employee.dateOfRetirement);
  switch (employee.reasonForRetirement) {
    case "DEATH_AS_EMPLOYEE":
      dtEnd = new Date(employee.dateOfDeath);
      break;
  }
  var monthsDaysInRange = getMonthsDaysInRange(dtStart, dtEnd);
  months = Number(monthsDaysInRange.split("|")[0]);
  days = Number(monthsDaysInRange.split("|")[1]);

  if (days >= 30) {
    months += 1;
    days -= 30;
  }

  years = Math.trunc(months / 12);
  months -= years * 12;

  gqs = years + "|" + months + "|" + days;

  return gqs;
};

var getGQSYear = exports.getGQSYear = function getGQSYear(employee) {
  var gqsYears = 0;

  var gqs = getGQS(employee);
  if (gqs) {
    gqsYears = Number(gqs.split("|")[0]);
  }

  return gqsYears;
};

var getGQSMonth = exports.getGQSMonth = function getGQSMonth(employee) {
  var gqsMonths = 0;

  var gqs = getGQS(employee);
  if (gqs) {
    gqsMonths = Number(gqs.split("|")[1]);
  }

  return gqsMonths;
};

var getGQSDay = exports.getGQSDay = function getGQSDay(employee) {
  var gqsDays = 0;

  var gqs = getGQS(employee);
  if (gqs) {
    gqsDays = Number(gqs.split("|")[2]);
  }

  return gqsDays;
};

var getMonthsDaysInRange = exports.getMonthsDaysInRange = function getMonthsDaysInRange(dtStart, dtEnd) {
  var startDate = new Date(dtStart);
  var endDate = new Date(dtEnd);
  var days = 0;
  var months = 0;
  var tempMonth = 0;
  var tempYear = 0;
  var Months = 0;
  var Days = 0;

  if (startDate.getDate() == 1 && isDateLastDayOfMonth(endDate)) {

    Months = endDate.getFullYear() * 12 + endDate.getMonth() - (startDate.getFullYear() * 12 + startDate.getMonth() - 1);
    Days = 0;
  } else {
    if (endDate.getDate() < startDate.getDate()) {
      days = endDate.getDate() + 30 + 1 - startDate.getDate();
      tempMonth = endDate.getMonth() - 1;

      if (tempMonth < startDate.getMonth()) {
        tempMonth = tempMonth + 12;
        tempYear = endDate.getFullYear() - 1;
        months += tempMonth - startDate.getMonth();
        months += (tempYear - startDate.getFullYear()) * 12;
      } else {
        months += tempMonth - startDate.getMonth();
        months += (endDate.getFullYear() - startDate.getFullYear()) * 12;
      }
    } else {
      days = endDate.getDate() + 1 - startDate.getDate();
      tempMonth = endDate.getMonth();

      if (tempMonth < startDate.getMonth()) {
        tempMonth = tempMonth + 12;
        tempYear = endDate.getFullYear() - 1;
        months += tempMonth - startDate.getMonth();
        months += (tempYear - startDate.getFullYear()) * 12;
      } else {
        months += tempMonth - startDate.getMonth();
        months += (endDate.getFullYear() - startDate.getFullYear()) * 12;
      }
    }

    Months = months;
    Days = days;
    /*
    let LastDayofMonth = lastDayOfMonth(endDate.getMonth(), endDate.getFullYear());      
    if (Days >= LastDayofMonth)
    {
        Months = Months +1;
        Days = Days- LastDayofMonth;
    } 
    */
  }

  var monthDays = Months + "|" + Days;

  return monthDays;
};

var isDateLastDayOfMonth = exports.isDateLastDayOfMonth = function isDateLastDayOfMonth(date) {
  var isDateLastDayOfMonth = false;
  if (new Date(date).getDate() == lastDayOfMonth(new Date(date).getMonth() + 1, new Date(date).getFullYear())) {
    isDateLastDayOfMonth = true;
  }
  return isDateLastDayOfMonth;
};

var lastDayOfMonth = exports.lastDayOfMonth = function lastDayOfMonth(month, year) {
  var lastDayOfMonth = 30;
  switch (month) {
    case 1:
    case 3:
    case 5:
    case 7:
    case 8:
    case 10:
    case 12:
      lastDayOfMonth = 31;
      break;
    case 4:
    case 6:
    case 9:
    case 11:
      lastDayOfMonth = 30;
      break;
    case 2:
      if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
        lastDayOfMonth = 29;
      } else {
        lastDayOfMonth = 28;
      }
      break;

  }
  return lastDayOfMonth;
};

var getDAPercentage = exports.getDAPercentage = function getDAPercentage(employee, mdms) {
  var value = 0;
  var mdmsDAPercentage = (0, _get2.default)(mdms, "MdmsRes.pension.DAPercentage");
  //const mdmsDAPercentage=get(mdms,"DAPercentage");  

  var daPercentageList = void 0;
  if (employee.businessService == "RRP_SERVICE") {
    daPercentageList = (0, _filter2.default)(mdmsDAPercentage, function (x) {
      return (0, _gte2.default)(new Date(employee.dateOfRetirement), new Date(x.startDate)) && ((0, _lte2.default)(new Date(employee.dateOfRetirement), new Date(x.endDate)) && x.endDate != null || x.endDate == null);
    });
  } else {

    daPercentageList = (0, _filter2.default)(mdmsDAPercentage, function (x) {
      return (0, _gte2.default)(new Date(employee.dateOfDeath), new Date(x.startDate)) && ((0, _lte2.default)(new Date(employee.dateOfDeath), new Date(x.endDate)) && x.endDate != null || x.endDate == null);
    });
  }

  if (daPercentageList && daPercentageList.length > 0) {
    value = daPercentageList[0] ? daPercentageList[0].value : 0;
  } else {
    /* daPercentageList=filter(mdmsDAPercentage,function(x){return gte(new Date(employee.dateOfRetirement),new Date(x.startDate)) 
     && x.endDate==null;});
      if(daPercentageList && daPercentageList.length>0){
      value=daPercentageList[0]? daPercentageList[0].value:0;
    } */
    value = 0;
  }

  return value;
};

var getCommutationPercentage = exports.getCommutationPercentage = function getCommutationPercentage(employee, mdms) {
  var value = 0;
  var mdmsCommutationPercentage = (0, _get2.default)(mdms, "MdmsRes.pension.CommutationPercentage");
  //const mdmsCommutationPercentage=get(mdms,"CommutationPercentage");  
  var commutationPercentageList = (0, _filter2.default)(mdmsCommutationPercentage, function (x) {
    return (0, _gte2.default)(new Date(employee.dateOfRetirement), new Date(x.startDate)) && (0, _lte2.default)(new Date(employee.dateOfRetirement), new Date(x.endDate)) && x.endDate != null;
  });

  if (commutationPercentageList && commutationPercentageList.length > 0) {
    value = commutationPercentageList[0] ? commutationPercentageList[0].value : 0;
  } else {
    commutationPercentageList = (0, _filter2.default)(mdmsCommutationPercentage, function (x) {
      return (0, _gte2.default)(new Date(employee.dateOfRetirement), new Date(x.startDate)) && x.endDate == null;
    });

    if (commutationPercentageList && commutationPercentageList.length > 0) {
      value = commutationPercentageList[0] ? commutationPercentageList[0].value : 0;
    }
  }

  return value;
};

var getCommutationMultiplier = exports.getCommutationMultiplier = function getCommutationMultiplier(employee, mdms) {
  var value = 0;
  //let age=getAge(employee.dob);
  var age = getAgeOnRetirementDate(employee.dob, employee.dateOfRetirement);
  var ageOnNextBirthDay = age + 1;

  var mdmsCommutationMultiplier = (0, _get2.default)(mdms, "MdmsRes.pension.CommutationMultiplier");
  //const mdmsCommutationMultiplier=get(mdms,"CommutationMultiplier"); 
  var commutationMultiplierList = (0, _filter2.default)(mdmsCommutationMultiplier, function (x) {
    return x.ageOnNextBirthDay == ageOnNextBirthDay;
  });

  if (commutationMultiplierList && commutationMultiplierList.length > 0) {
    value = commutationMultiplierList[0] ? commutationMultiplierList[0].value : 0;
  }

  return value;
};

var getIRPercentage = exports.getIRPercentage = function getIRPercentage(employee, mdms) {
  var value = 0;
  var mdmsIRPercentage = (0, _get2.default)(mdms, "MdmsRes.pension.IRPercentage");
  //const mdmsIRPercentage=get(mdms,"IRPercentage");  


  var irPercentageList = void 0;

  if (employee.businessService == "RRP_SERVICE") {
    irPercentageList = (0, _filter2.default)(mdmsIRPercentage, function (x) {
      return (0, _gte2.default)(new Date(employee.dateOfRetirement), new Date(x.startDate)) && ((0, _lte2.default)(new Date(employee.dateOfRetirement), new Date(x.endDate)) && x.endDate != null || x.endDate == null);
    });
  } else {
    irPercentageList = (0, _filter2.default)(mdmsIRPercentage, function (x) {
      return (0, _gte2.default)(new Date(employee.dateOfDeath), new Date(x.startDate)) && ((0, _lte2.default)(new Date(employee.dateOfDeath), new Date(x.endDate)) && x.endDate != null || x.endDate == null);
    });
  }

  if (irPercentageList && irPercentageList.length > 0) {
    value = irPercentageList[0] ? irPercentageList[0].value : 0;
  } else {
    /* irPercentageList=filter(mdmsIRPercentage,function(x){return gte(new Date(employee.dateOfRetirement),new Date(x.startDate)) 
      && x.endDate==null;});
      if(irPercentageList && irPercentageList.length>0){
      value=irPercentageList[0]? irPercentageList[0].value:0;
    } */

    value = 0;
  }

  return value;
};

var getAdditionalPensionPercentage = exports.getAdditionalPensionPercentage = function getAdditionalPensionPercentage(employee, mdms) {
  var value = 0;
  //let age=getAge(employee.dob);
  var age = getAgeOnRetirementDate(employee.dob, employee.dateOfRetirement);
  var mdmsAdditionalPensionPercentage = (0, _get2.default)(mdms, "MdmsRes.pension.AdditionalPensionPercentage");
  //const mdmsAdditionalPensionPercentage=get(mdms,"AdditionalPensionPercentage");
  var additionalPensionPercentageList = (0, _filter2.default)(mdmsAdditionalPensionPercentage, function (x) {
    return (0, _gte2.default)(age, x.fromAge) && (0, _lte2.default)(age, Number(x.toAge)) && x.toAge != null;
  });

  if (additionalPensionPercentageList && additionalPensionPercentageList.length > 0) {
    value = additionalPensionPercentageList[0].value;
  } else {
    additionalPensionPercentageList = (0, _filter2.default)(mdmsAdditionalPensionPercentage, function (x) {
      return (0, _gte2.default)(age, x.fromAge) && x.toAge == null;
    });

    if (additionalPensionPercentageList && additionalPensionPercentageList.length > 0) {
      value = additionalPensionPercentageList[0].value;
    }
  }

  return value;
};

var getPensionConfigurationValue = exports.getPensionConfigurationValue = function getPensionConfigurationValue(key, mdms) {
  var value = "";

  var mdmsConfigurations = (0, _get2.default)(mdms, "MdmsRes.pension.PensionConfig");
  //const mdmsConfigurations=get(mdms,"configurations");
  var configurationsList = (0, _filter2.default)(mdmsConfigurations, function (x) {
    return x.key == key;
  });

  if (configurationsList && configurationsList.length > 0) {
    value = configurationsList[0].value;
  }

  return value;
};

var getDOJ = exports.getDOJ = function getDOJ(employee) {
  var doj = "";
  var serviceHistory = employee.serviceHistory[0];
  doj = serviceHistory.serviceFrom;
  return doj;
};

var getAge = exports.getAge = function getAge(dob) {
  var age = 0;
  var currentDateTime = new Date();
  var today = new Date(currentDateTime.getFullYear(), currentDateTime.getMonth(), currentDateTime.getDate());
  var currentYearDOB = new Date(today.getFullYear(), new Date(dob).getMonth(), new Date(dob).getDate());
  if (currentYearDOB > today) {
    age = today.getFullYear() - new Date(dob).getFullYear() - 1;
  } else {
    age = today.getFullYear() - new Date(dob).getFullYear();
  }

  return age;
};

var getAgeOnRetirementDate = exports.getAgeOnRetirementDate = function getAgeOnRetirementDate(dob, dateOfRetirement) {
  var age = 0;
  //let currentDateTime=new Date();
  var today = new Date(dateOfRetirement);
  var dobOnRetirementYear = new Date(today.getFullYear(), new Date(dob).getMonth(), new Date(dob).getDate());

  if (dobOnRetirementYear > today) {
    age = today.getFullYear() - new Date(dob).getFullYear() - 1;
  } else {
    age = today.getFullYear() - new Date(dob).getFullYear();
  }

  return age;
};

var isEldestDependent = exports.isEldestDependent = function isEldestDependent(dependent, dependents) {
  var isEldestDependent = false;
  var dependentAge = getAge(dependent.dob);

  var eligibleDependents = (0, _filter2.default)(dependents, function (x) {
    return (x.relationship == "SON" || x.relationship == "DAUGHTER") && x.isEligibleForGratuity == true;
  });
  if (eligibleDependents && eligibleDependents.length == 1) {
    isEldestDependent = true;
  } else {
    for (var i = 0; i < eligibleDependents.length; i++) {
      eligibleDependents[i].age = getAge(eligibleDependents[i].dob);
    }
    var eldestGratuityEligibleDependent = (0, _orderBy2.default)(eligibleDependents, ['age'], ['desc']);
    if (eldestGratuityEligibleDependent[0].dob == dependent.dob && eldestGratuityEligibleDependent[0].name == dependent.name && eldestGratuityEligibleDependent[0].relationship == dependent.relationship) {
      isEldestDependent = true;
    }
  }

  return isEldestDependent;
};

var getAdditionalPensionPercentageForFamily = exports.getAdditionalPensionPercentageForFamily = function getAdditionalPensionPercentageForFamily(dependents, mdms) {
  var value = 0;
  var pensionEligibleDependents = (0, _filter2.default)(dependents, function (x) {
    return x.isEligibleForPension == true;
  });

  if (pensionEligibleDependents && pensionEligibleDependents.length > 0) {
    var dependent = pensionEligibleDependents[0];
    var age = getAge(dependent.dob);
    var mdmsAdditionalPensionPercentage = (0, _get2.default)(mdms, "MdmsRes.pension.AdditionalPensionPercentage");
    //const mdmsAdditionalPensionPercentage=get(mdms,"AdditionalPensionPercentage");
    var additionalPensionPercentageList = (0, _filter2.default)(mdmsAdditionalPensionPercentage, function (x) {
      return (0, _gte2.default)(age, x.fromAge) && x.toAge == null;
    });

    if (additionalPensionPercentageList && additionalPensionPercentageList.length > 0) {
      value = additionalPensionPercentageList[0].value;
    } else {
      additionalPensionPercentageList = (0, _filter2.default)(mdmsAdditionalPensionPercentage, function (x) {
        return (0, _gte2.default)(age, x.fromAge) && x.toAge == null;
      });

      if (additionalPensionPercentageList && additionalPensionPercentageList.length > 0) {
        value = additionalPensionPercentageList[0].value;
      }
    }
  }

  return value;
};
//# sourceMappingURL=calculationHelper.js.map