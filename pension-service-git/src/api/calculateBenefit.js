import { Router } from "express";
import producer from "../kafka/producer";
import { requestInfoToResponseInfo, searchPensionWorkflow,epochToYmd,calculateBenefit,convertDateToEpoch, convertDateToEpochForDeathDate} from "../utils";
import envVariables from "../envVariables";
import mdmsData from "../utils/mdmsData";
import { addUUIDAndAuditDetails } from "../utils/create";
import isEmpty from "lodash/isEmpty";
import set from "lodash/set";
import get from "lodash/get";
import orderBy from "lodash/orderBy";
import { intConversion} from "../utils/search";
const asyncHandler = require("express-async-handler");

import logger from "../config/logger";

export default ({ config, db }) => {
  let api = Router();
  api.post(
    "/_calculateBenefit",
    asyncHandler(async ({ body }, res, next) => {              
      //getting mdms data
      
           
      let tenantId=body.ProcessInstances[0].tenantId;   
      let businessService=body.ProcessInstances[0].businessService;
      let businessId=body.ProcessInstances[0].businessId;
      
      let workflowSearchResponse = await searchPensionWorkflow(body.RequestInfo, tenantId,businessId);
      
      logger.debug("workflowSearchResponse", JSON.stringify(workflowSearchResponse));
              
        let processInstance=workflowSearchResponse.ProcessInstances[0];
        let reasonForRetirement="";
        switch(businessService){
          case envVariables.EGOV_PENSION_RRP_BUSINESS_SERVICE:
            reasonForRetirement=processInstance.employeeOtherDetails.reasonForRetirement;
            break;
          case envVariables.EGOV_PENSION_DOE_BUSINESS_SERVICE:
            reasonForRetirement="DEATH_AS_EMPLOYEE";
            break;
          case envVariables.EGOV_PENSION_DOP_BUSINESS_SERVICE:
            reasonForRetirement="DEATH_AS_PENSIONER";
            break;

        }

        let employee ={
          dob : epochToYmd(intConversion(processInstance.employee.user.dob)),
          employeeType: processInstance.employee.employeeType,
          dateOfRetirement: epochToYmd(intConversion(processInstance.employee.dateOfRetirement)),  
          dateOfDeath : processInstance.employee.dateOfDeath && processInstance.employee.dateOfDeath!=0?epochToYmd(intConversion(processInstance.employee.dateOfDeath)):null,
          reasonForRetirement : reasonForRetirement,//processInstance.employeeOtherDetails.reasonForRetirement, 
          lastDesignation : "",
          totalNoPayLeavesDays :processInstance.employeeOtherDetails.totalNoPayLeavesDays!=null? Number(processInstance.employeeOtherDetails.totalNoPayLeavesDays):0 , 
          totalNoPayLeavesMonths :processInstance.employeeOtherDetails.totalNoPayLeavesMonths!=null? Number(processInstance.employeeOtherDetails.totalNoPayLeavesMonths):0 , 
          totalNoPayLeavesYears :processInstance.employeeOtherDetails.totalNoPayLeavesYears!=null? Number(processInstance.employeeOtherDetails.totalNoPayLeavesYears):0 , 
          lpd : processInstance.employeeOtherDetails.lpd!=null? Number(processInstance.employeeOtherDetails.lpd):0, 
          fma : processInstance.employeeOtherDetails.fma!=null? Number(processInstance.employeeOtherDetails.fma):0, 
          dues : processInstance.employeeOtherDetails.dues!=null? Number(processInstance.employeeOtherDetails.dues):0, 
          medicalRelief : processInstance.employeeOtherDetails.medicalRelief!=null? Number(processInstance.employeeOtherDetails.medicalRelief):0,          
          miscellaneous : processInstance.employeeOtherDetails.miscellaneous!=null? Number(processInstance.employeeOtherDetails.miscellaneous):0,
          overPayment :processInstance.employeeOtherDetails.overPayment!=null? Number(processInstance.employeeOtherDetails.overPayment):0,
          incomeTax :processInstance.employeeOtherDetails.incomeTax!=null? Number(processInstance.employeeOtherDetails.incomeTax):0,
          cess :processInstance.employeeOtherDetails.cess!=null? Number(processInstance.employeeOtherDetails.cess):0,
          isCommutationOpted : processInstance.employeeOtherDetails.isCommutationOpted, 
          isEmploymentActive : processInstance.employeeOtherDetails.isEmploymentActive,
          isConvictedSeriousCrimeOrGraveMisconduct : processInstance.employeeOtherDetails.isConvictedSeriousCrimeOrGraveMisconduct,
          isAnyJudicialProceedingIsContinuing : processInstance.employeeOtherDetails.isAnyJudicialProceedingIsContinuing,
          isAnyMisconductInsolvencyInefficiency : processInstance.employeeOtherDetails.isAnyMisconductInsolvencyInefficiency,
          isEmployeeDiesInTerroristAttack : processInstance.employeeOtherDetails.isEmployeeDiesInTerroristAttack,
          isEmployeeDiesInAccidentalDeath : processInstance.employeeOtherDetails.isEmployeeDiesInAccidentalDeath,
          isDuesPresent : processInstance.employeeOtherDetails.isDuesPresent,
          isDuesAmountDecided : processInstance.employeeOtherDetails.isDuesAmountDecided,
          isTakenMonthlyPensionAndGratuity : processInstance.employeeOtherDetails.isTakenMonthlyPensionAndGratuity,
          isTakenGratuityCommutationTerminalBenefit : processInstance.employeeOtherDetails.isTakenGratuityCommutationTerminalBenefit,
          isTakenCompensationPensionAndGratuity : processInstance.employeeOtherDetails.isTakenCompensationPensionAndGratuity,
          diesInExtremistsDacoitsSmugglerAntisocialAttack : processInstance.employeeOtherDetails.diesInExtremistsDacoitsSmugglerAntisocialAttack,
          isCompassionatePensionGranted : processInstance.employeeOtherDetails.isCompassionatePensionGranted,
          noDuesForAvailGovtAccomodation : processInstance.employeeOtherDetails.noDuesForAvailGovtAccomodation,
          employeeGroup : processInstance.employeeOtherDetails.employeeGroup,
          employeeDisability:{
            disabilityPercentage:processInstance.employeeDisability.disabilityPercentage!=null?Number(processInstance.employeeDisability.disabilityPercentage):0,
            woundExtraordinaryPension:processInstance.employeeDisability.woundExtraordinaryPension!=null?Number(processInstance.employeeDisability.woundExtraordinaryPension):0,
            attendantAllowanceGranted:processInstance.employeeDisability.attendantAllowanceGranted!=null?processInstance.employeeDisability.attendantAllowanceGranted:false
          },
          serviceHistory: [],
          dependents: []
        };

        let serviceHistory=[];
        for (var i = 0; i < processInstance.employee.serviceHistory.length; i++) {                    
          serviceHistory.push({
            serviceFrom:  epochToYmd(intConversion(processInstance.employee.serviceHistory[i].serviceFrom)),
            serviceTo: processInstance.employee.serviceHistory[i].serviceTo? epochToYmd(intConversion(processInstance.employee.serviceHistory[i].serviceTo)):null
          });
        } 
        employee.serviceHistory=serviceHistory;

        let dependents=[];
        if(processInstance.dependents){
          for (var i = 0; i < processInstance.dependents.length; i++) {                    
            dependents.push({
              name:  processInstance.dependents[i].name,
              dob:  epochToYmd(intConversion(processInstance.dependents[i].dob)),
              address:  processInstance.dependents[i].address,
              mobileNumber:  processInstance.dependents[i].mobileNumber,
              relationship:  processInstance.dependents[i].relationship,
              isDisabled:  processInstance.dependents[i].isDisabled,
              maritalStatus:  processInstance.dependents[i].maritalStatus,
              isHollyDependent:  processInstance.dependents[i].isHollyDependent,
              noSpouseNoChildren:  processInstance.dependents[i].noSpouseNoChildren,
              isGrandChildFromDeceasedSon:  processInstance.dependents[i].isGrandChildFromDeceasedSon,
              isEligibleForGratuity:  processInstance.dependents[i].isEligibleForGratuity,
              isEligibleForPension:  processInstance.dependents[i].isEligibleForPension,
              gratuityPercentage:  processInstance.dependents[i].gratuityPercentage
            });
          } 
        }        
        employee.dependents=dependents;

        /*

        let assignments=[];
        for (var i = 0; i < processInstance.employee.assignments.length; i++) {                    
            assignments.push({
              fromDate:  epochToYmd(intConversion(processInstance.employee.assignments[i].fromDate)),
              toDate: processInstance.employee.assignments[i].toDate? epochToYmd(intConversion(processInstance.employee.assignments[i].toDate)):null
            });
          } 
        assignments=orderBy(processInstance.employee.assignments,['fromDate'],['desc']);
        let lastAssignment=assignments[0];
        employee.lastDesignation=lastAssignment.designation;
        */
        employee.lastDesignation="";
              

        body.ProcessInstances[0].employee=employee;
        body.ProcessInstances[0].employee.businessService=businessService;
              
        

        let benefitRespone=await calculateBenefit(body);
        logger.debug("benefitRespone",JSON.stringify(benefitRespone));

        let benefits=benefitRespone.EmployeeBenefits[0].benefits;
        let pensionCalculationDetails=benefitRespone.EmployeeBenefits[0].pensionCalculationDetails;
        //let notifications=benefitRespone.EmployeeBenefits[0].notifications;

        for (var i = 0; i < benefits.length; i++) {             
            switch(String(benefits[i].benefitCode).toUpperCase()){  
               
               case "IR":
                  pensionCalculationDetails.interimReliefLpdSystem=benefits[i].finalBenefitValue;
                  //pensionCalculationDetails.interimReliefApplicable=benefits[i].benefitApplicable;
                  //pensionCalculationDetails.interimReliefExpression=benefits[i].benefitFormulaExpression;
                  break;     
                
                case "PENSION_IR":
                  pensionCalculationDetails.interimReliefSystem=benefits[i].finalBenefitValue;
                  pensionCalculationDetails.interimReliefApplicable=benefits[i].benefitApplicable;
                  pensionCalculationDetails.interimReliefExpression=benefits[i].benefitFormulaExpression;
                  break;                        
                case "BASIC_PENSION":
                  pensionCalculationDetails.basicPensionSystem=benefits[i].finalBenefitValue;  
                  pensionCalculationDetails.basicPensionApplicable=benefits[i].benefitApplicable;
                  pensionCalculationDetails.basicPensionExpression=benefits[i].benefitFormulaExpression;                
                  break;
                case "COMMUTED_PENSION":
                  pensionCalculationDetails.commutedPensionSystem=benefits[i].finalBenefitValue;   
                  pensionCalculationDetails.commutedPensionApplicable=benefits[i].benefitApplicable;
                  pensionCalculationDetails.commutedPensionExpression=benefits[i].benefitFormulaExpression;                                  
                  break;
                case "PENSION_DEDUCTION":
                  pensionCalculationDetails.pensionDeductionsSystem=benefits[i].finalBenefitValue;
                  pensionCalculationDetails.pensionDeductionsApplicable=benefits[i].benefitApplicable;
                  pensionCalculationDetails.pensionDeductionsExpression=benefits[i].benefitFormulaExpression;                  
                  break;                  
                case "COMMUTATION_VALUE":
                  pensionCalculationDetails.commutedValueSystem=benefits[i].finalBenefitValue;
                  pensionCalculationDetails.commutationValueApplicable=benefits[i].benefitApplicable;
                  pensionCalculationDetails.commutationValueExpression=benefits[i].benefitFormulaExpression;
                  break; 
                   
                case "DA":
                  pensionCalculationDetails.daLpdSystem=benefits[i].finalBenefitValue;
                  break;
                
                case "PENSION_DA":
                  pensionCalculationDetails.daSystem=benefits[i].finalBenefitValue;
                  pensionCalculationDetails.daApplicable=benefits[i].benefitApplicable;
                  pensionCalculationDetails.daExpression=benefits[i].benefitFormulaExpression; 
                  break;       
                case "DCRG":
                  pensionCalculationDetails.dcrgSystem=benefits[i].finalBenefitValue;
                  pensionCalculationDetails.dcrgApplicable=benefits[i].benefitApplicable;
                  pensionCalculationDetails.dcrgExpression=benefits[i].benefitFormulaExpression;
                  break;      
                case "ADDITIONAL_PENSION":
                  pensionCalculationDetails.additionalPensionSystem=benefits[i].finalBenefitValue;   
                  pensionCalculationDetails.additionalPensionApplicable=benefits[i].benefitApplicable;
                  pensionCalculationDetails.additionalPensionExpression=benefits[i].benefitFormulaExpression;               
                  break;  
                case "COMPASSIONATE_PENSION":
                  pensionCalculationDetails.compassionatePensionSystem=benefits[i].finalBenefitValue; 
                  pensionCalculationDetails.compassionatePensionApplicable=benefits[i].benefitApplicable;
                  pensionCalculationDetails.compassionatePensionExpression=benefits[i].benefitFormulaExpression;                 
                  break;  
                case "COMPENSATION_PENSION":
                  pensionCalculationDetails.compensationPensionSystem=benefits[i].finalBenefitValue;    
                  pensionCalculationDetails.compensationPensionApplicable=benefits[i].benefitApplicable;
                  pensionCalculationDetails.compensationPensionExpression=benefits[i].benefitFormulaExpression;                               
                  break;
                case "TERMINAL_BENEFIT":
                  pensionCalculationDetails.terminalBenefitSystem=benefits[i].finalBenefitValue;
                  pensionCalculationDetails.terminalBenefitApplicable=benefits[i].benefitApplicable;
                  pensionCalculationDetails.terminalBenefitExpression=benefits[i].benefitFormulaExpression;                  
                  break;                   
                case "DCRG_DUES_DEDUCTION":
                  pensionCalculationDetails.duesDeductionsSystem=benefits[i].finalBenefitValue;     
                  pensionCalculationDetails.duesDeductionsApplicable=benefits[i].benefitApplicable;
                  pensionCalculationDetails.duesDeductionsExpression=benefits[i].benefitFormulaExpression;               
                  break;          
                case "NET_DEDUCTION":
                  pensionCalculationDetails.netDeductionsSystem=benefits[i].finalBenefitValue;
                  pensionCalculationDetails.netDeductionsApplicable=benefits[i].benefitApplicable;
                  pensionCalculationDetails.netDeductionsExpression=benefits[i].benefitFormulaExpression;                    
                  break;
                case "TOTAL_PENSION":
                  pensionCalculationDetails.totalPensionSystem=benefits[i].finalBenefitValue;   
                  pensionCalculationDetails.totalPensionApplicable=benefits[i].benefitApplicable;
                  pensionCalculationDetails.totalPensionExpression=benefits[i].benefitFormulaExpression;               
                  break; 
                case "FINAL_CALCULATED_PENSION":
                  pensionCalculationDetails.finalCalculatedPensionSystem=benefits[i].finalBenefitValue;   
                  pensionCalculationDetails.finalCalculatedPensionApplicable=benefits[i].benefitApplicable;
                  pensionCalculationDetails.finalCalculatedPensionExpression=benefits[i].benefitFormulaExpression;                    
                  break; 
                case "FINAL_CALCULATED_GRATUITY":
                  pensionCalculationDetails.finalCalculatedGratuitySystem=benefits[i].finalBenefitValue;  
                  pensionCalculationDetails.finalCalculatedGratuityApplicable=benefits[i].benefitApplicable;
                  pensionCalculationDetails.finalCalculatedGratuityExpression=benefits[i].benefitFormulaExpression;                       
                  break; 
                case "FAMILY_PENSION_1":
                  pensionCalculationDetails.familyPensionISystem=benefits[i].finalBenefitValue; 
                  pensionCalculationDetails.familyPensionIApplicable=benefits[i].benefitApplicable;
                  pensionCalculationDetails.familyPensionIExpression=benefits[i].benefitFormulaExpression;                  
                  break;
                case "FAMILY_PENSION_2":
                  pensionCalculationDetails.familyPensionIISystem=benefits[i].finalBenefitValue;    
                  pensionCalculationDetails.familyPensionIIApplicable=benefits[i].benefitApplicable;
                  pensionCalculationDetails.familyPensionIIExpression=benefits[i].benefitFormulaExpression;               
                  break;
                case "PENSIONER_FAMILY_PENSION":
                  pensionCalculationDetails.pensionerFamilyPensionSystem=benefits[i].finalBenefitValue;     
                  pensionCalculationDetails.pensionerFamilyPensionApplicable=benefits[i].benefitApplicable;
                  pensionCalculationDetails.pensionerFamilyPensionExpression=benefits[i].benefitFormulaExpression;             
                  break;                
                case "FAMILY_PENSION_1_START_DATE":
                  pensionCalculationDetails.familyPensionIStartDateSystem=benefits[i].finalBenefitValue!=null?intConversion(convertDateToEpochForDeathDate(benefits[i].finalBenefitValue,"dob")) :null;                                    
                  break;
                case "FAMILY_PENSION_1_END_DATE":
                  pensionCalculationDetails.familyPensionIEndDateSystem=benefits[i].finalBenefitValue!=null?intConversion(convertDateToEpochForDeathDate(benefits[i].finalBenefitValue,"dob")):null;                                   
                  break;
                case "FAMILY_PENSION_2_START_DATE":
                  pensionCalculationDetails.familyPensionIIStartDateSystem=benefits[i].finalBenefitValue!=null?intConversion(convertDateToEpochForDeathDate(benefits[i].finalBenefitValue,"dob")):null;                                   
                  break;                                             
                case "EX_GRATIA":
                  pensionCalculationDetails.exGratiaSystem=benefits[i].finalBenefitValue;    
                  pensionCalculationDetails.exGratiaApplicable=benefits[i].benefitApplicable;
                  pensionCalculationDetails.exGratiaExpression=benefits[i].benefitFormulaExpression;               
                  break;
                case "PROVISIONAL_PENSION":
                  pensionCalculationDetails.provisionalPensionSystem=benefits[i].finalBenefitValue;
                  pensionCalculationDetails.provisionalPensionApplicable=benefits[i].benefitApplicable;
                  pensionCalculationDetails.provisionalPensionExpression=benefits[i].benefitFormulaExpression;                  
                  break;
                case "INVALID_PENSION":
                  pensionCalculationDetails.invalidPensionSystem=benefits[i].finalBenefitValue;
                  pensionCalculationDetails.invalidPensionApplicable=benefits[i].benefitApplicable;
                  pensionCalculationDetails.invalidPensionExpression=benefits[i].benefitFormulaExpression;                  
                  break;
                case "WOUND_EXTRAORDINARY_PENSION":
                  pensionCalculationDetails.woundExtraordinaryPensionSystem=benefits[i].finalBenefitValue;
                  pensionCalculationDetails.woundExtraordinaryPensionApplicable=benefits[i].benefitApplicable;
                  pensionCalculationDetails.woundExtraordinaryPensionExpression=benefits[i].benefitFormulaExpression;                  
                  break;
                case "ATTENDANT_ALLOWANCE":
                  pensionCalculationDetails.attendantAllowanceSystem=benefits[i].finalBenefitValue;
                  pensionCalculationDetails.attendantAllowanceApplicable=benefits[i].benefitApplicable;
                  pensionCalculationDetails.attendantAllowanceExpression=benefits[i].benefitFormulaExpression;                  
                  break;

            }
            
        }

        
        /*
        let notificationText="";
        for (var i = 0; i < notifications.length; i++) {    
          notificationText=`${notificationText}${notifications[i].notificationText} `
        }
        */

        let processInstances=[];
        processInstances.push({
            pensionCalculationDetails: pensionCalculationDetails,
            pensionCalculationUpdateDetails: {
                nqsYearVerified: pensionCalculationDetails.nqsYearSystem,
                nqsMonthVerified: pensionCalculationDetails.nqsMonthSystem,
                nqsDayVerified: pensionCalculationDetails.nqsDaySystem,
                basicPensionVerified: pensionCalculationDetails.basicPensionSystem,
                pensionDeductionsVerified: pensionCalculationDetails.pensionDeductionsSystem,
                additionalPensionVerified: pensionCalculationDetails.additionalPensionSystem,
                commutedPensionVerified: pensionCalculationDetails.commutedPensionSystem,
                commutedValueVerified: pensionCalculationDetails.commutedValueSystem,
                familyPensionIVerified: pensionCalculationDetails.familyPensionISystem,
                familyPensionIIVerified: pensionCalculationDetails.familyPensionIISystem,
                dcrgVerified: pensionCalculationDetails.dcrgSystem,
                netDeductionsVerified: pensionCalculationDetails.netDeductionsSystem,
                totalPensionVerified: pensionCalculationDetails.totalPensionSystem,
                finalCalculatedPensionVerified: pensionCalculationDetails.finalCalculatedPensionSystem,
                interimReliefVerified: pensionCalculationDetails.interimReliefSystem,
                daVerified: pensionCalculationDetails.daSystem,
                duesDeductionsVerified: pensionCalculationDetails.duesDeductionsSystem,
                compassionatePensionVerified: pensionCalculationDetails.compassionatePensionSystem,
                compensationPensionVerified: pensionCalculationDetails.compensationPensionSystem,
                terminalBenefitVerified: pensionCalculationDetails.terminalBenefitSystem,
                finalCalculatedGratuityVerified: pensionCalculationDetails.finalCalculatedGratuitySystem,                
                familyPensionIStartDateVerified: pensionCalculationDetails.familyPensionIStartDateSystem,
                familyPensionIEndDateVerified: pensionCalculationDetails.familyPensionIEndDateSystem,
                familyPensionIIStartDateVerified: pensionCalculationDetails.familyPensionIIStartDateSystem,
                exGratiaVerified: pensionCalculationDetails.exGratiaSystem,
                pensionerFamilyPensionVerified: pensionCalculationDetails.pensionerFamilyPensionSystem,                
                provisionalPensionVerified: pensionCalculationDetails.provisionalPensionSystem,
                invalidPensionVerified: pensionCalculationDetails.invalidPensionSystem,
                woundExtraordinaryPensionVerified: pensionCalculationDetails.woundExtraordinaryPensionSystem,
                attendantAllowanceVerified: pensionCalculationDetails.attendantAllowanceSystem,
                gqsYearVerified: pensionCalculationDetails.gqsYearSystem,
                gqsMonthVerified: pensionCalculationDetails.gqsMonthSystem,
                gqsDayVerified: pensionCalculationDetails.gqsDaySystem,
                notificationTextVerified: pensionCalculationDetails.notificationTextSystem,
                interimReliefLpdVerified: pensionCalculationDetails.interimReliefLpdSystem,
                daLpdVerified: pensionCalculationDetails.daLpdSystem
            }//,
            //notifications: {
            //  notificationText: notificationText
            //}
        });

        let response = {
          ResponseInfo: requestInfoToResponseInfo(body.RequestInfo, true),
          ProcessInstances: processInstances          
        };
        
        
        res.json(response);     
        
    })
  );
  return api;
};
