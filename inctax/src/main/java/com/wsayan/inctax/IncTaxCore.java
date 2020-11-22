package com.wsayan.inctax;

public class IncTaxCore {
    public static final String GENDER_MALE = "_male";
    public static final String GENDER_FEMALE = "_female";

    private static final int BASIC_PERCENTAGE = 60;
    private static final int HOUSE_RENT_PERCENTAGE = 30;
    private static final int CONVEYANCE_ALLOWANCE_PERCENTAGE = 4;
    private static final int MEDICAL_ALLOWANCE_PERCENTAGE = 6;
    private static final int HOUSE_RENT_DEDUCTABLE = 25000;
    private static final int CONVEYANCE_ALLOWANCE_DEDUCTABLE = 2500;
    private static final int MEDICAL_ALLOWANCE_DEDUCTABLE = 10000;

    private static final int TAX_CREDIT_RANGE_FIRST = 1000000;
    private static final int TAX_CREDIT_RANGE_SECOND = 3000000;
    private static final int TAX_CREDIT_PERCENTAGE_FIRST = 15;
    private static final int TAX_CREDIT_PERCENTAGE_SECOND = 12;
    private static final int TAX_CREDIT_PERCENTAGE_THIRD = 10;

    private static final int TAX_RANGE_FIRST = 300000;
    private static final int TAX_RANGE_FIRST_FEMALE = 350000;
    private static final int TAX_CREDIT_RANGE = 500000;

    private static final int TAX_STEP_SECOND = 100000;
    private static final int TAX_STEP_THIRD = 300000;
    private static final int TAX_STEP_FOURTH = 400000;
    private static final int TAX_STEP_FIFTH = 500000;

    private static final int TAX_RANGE_FIRST_PERCENTAGE = 0;
    private static final int TAX_RANGE_SECOND_PERCENTAGE = 10;
    private static final int TAX_RANGE_THIRD_PERCENTAGE = 15;
    private static final int TAX_RANGE_FOURTH_PERCENTAGE = 20;
    private static final int TAX_RANGE_FIFTH_PERCENTAGE = 25;
    private static final int TAX_RANGE_SIXTH_PERCENTAGE = 30;

    private static final int ALLOWABLE_INVESTMENT_PERCENTAGE = 25;
    private static final int ALLOWABLE_INVESTMENT_LIMIT = 15000000;

    private static final int NUMBER_OF_MONTHS = 12;

    public int calculateTaxByDefaultGross(String gender, int gross, int totalExtraBenefits,
                                          int totalAllowableInvestments) {
        int basic = generateBasic(gross, BASIC_PERCENTAGE);
        int houseRent = generateHouseRent(gross, HOUSE_RENT_PERCENTAGE);
        int conveyanceAllowance = generateConveyanceAllowance(gross, CONVEYANCE_ALLOWANCE_PERCENTAGE);
        int medicalAllowance = generateMedicalAllowance(gross, MEDICAL_ALLOWANCE_PERCENTAGE);

        int taxableIncome = calculateTaxableIncome(basic, houseRent, conveyanceAllowance, medicalAllowance, totalExtraBenefits);
        int taxCredit = calculateTaxCredit(gender, taxableIncome, totalAllowableInvestments);
        int taxLiablity = calculateTax(taxableIncome, gender);
        return taxLiablity - taxCredit;
    }

    private int calculateTaxCredit(String gender, int taxableIncome, int totalAllowableInvestments) {
        if (totalAllowableInvestments > ALLOWABLE_INVESTMENT_LIMIT) {
            totalAllowableInvestments = ALLOWABLE_INVESTMENT_LIMIT;
        }

        int allowablePercentage = (int) (((float) totalAllowableInvestments / (float) taxableIncome) * 100);
        if (allowablePercentage > ALLOWABLE_INVESTMENT_PERCENTAGE) {
            allowablePercentage = ALLOWABLE_INVESTMENT_PERCENTAGE;
        }

        int eligibleAllowableInvestment = taxableIncome * allowablePercentage / 100;

        int taxCredit = 0;
        if (taxableIncome < TAX_CREDIT_RANGE_FIRST) {
            taxCredit = eligibleAllowableInvestment * TAX_CREDIT_PERCENTAGE_FIRST / 100;
        } else if (taxableIncome > TAX_CREDIT_RANGE_FIRST && eligibleAllowableInvestment <= TAX_CREDIT_RANGE_SECOND) {
            taxCredit = (TAX_RANGE_FIRST * TAX_CREDIT_PERCENTAGE_FIRST / 100)
                    + ((eligibleAllowableInvestment - TAX_RANGE_FIRST) * TAX_CREDIT_PERCENTAGE_SECOND / 100);
        } else if (taxableIncome > TAX_CREDIT_RANGE_SECOND) {
            taxCredit = (TAX_RANGE_FIRST * TAX_CREDIT_PERCENTAGE_FIRST / 100)
                    + ((eligibleAllowableInvestment - TAX_RANGE_FIRST) * TAX_CREDIT_PERCENTAGE_SECOND / 100)
                    + ((eligibleAllowableInvestment - TAX_RANGE_FIRST - TAX_CREDIT_RANGE) * TAX_CREDIT_RANGE / 100);
        }

        return taxCredit;
    }

    private int calculateTaxableIncome(int basic, int houseRent, int conveyanceAllowance, int medicalAllowance, int totalExtraBenefits) {
        return (basic + deductHouseRentTax(houseRent, basic) + deductConveyanceAllowanceTax(conveyanceAllowance, basic) + deductMedicalAllowanceTax(medicalAllowance, basic)) * NUMBER_OF_MONTHS + totalExtraBenefits;
    }

    private int deductHouseRentTax(int houseRent, int basic) {
        if (houseRent < HOUSE_RENT_DEDUCTABLE || houseRent < basic / 2)
            return 0;

        return houseRent - HOUSE_RENT_DEDUCTABLE;
    }

    private int deductConveyanceAllowanceTax(int conveyanceAllowance, int basic) {
        if (conveyanceAllowance < CONVEYANCE_ALLOWANCE_DEDUCTABLE)
            return 0;

        return conveyanceAllowance - CONVEYANCE_ALLOWANCE_DEDUCTABLE;
    }

    private int deductMedicalAllowanceTax(int medicalAllowance, int basic) {
        if (medicalAllowance < MEDICAL_ALLOWANCE_DEDUCTABLE || medicalAllowance < basic / 10)
            return 0;

        return medicalAllowance - MEDICAL_ALLOWANCE_DEDUCTABLE;
    }

    private int generateBasic(int gross, int percentage) {
        return (percentage * gross) / 100;
    }

    private int generateHouseRent(int gross, int percentage) {
        return (percentage * gross) / 100;
    }

    private int generateConveyanceAllowance(int gross, int percentage) {
        return (percentage * gross) / 100;
    }

    private int generateMedicalAllowance(int gross, int percentage) {
        return (percentage * gross) / 100;
    }

    private int calculateTax(int taxableIncome, String gender) {
        int calcTax = 0;
        int taxable = 0;
        int genderBasedFirstRange = TAX_RANGE_FIRST;
        switch (gender) {
            case GENDER_MALE:
                genderBasedFirstRange = TAX_RANGE_FIRST;
                break;
            case GENDER_FEMALE:
                genderBasedFirstRange = TAX_RANGE_FIRST_FEMALE;
                break;
        }

        if (taxableIncome < genderBasedFirstRange) return 0;

        taxable = taxableIncome - genderBasedFirstRange;
        int taxRangeSecond = genderBasedFirstRange + TAX_STEP_SECOND;
        if (taxable > taxRangeSecond) {
            calcTax += taxRangeSecond * TAX_RANGE_SECOND_PERCENTAGE / 100;
        } else if (taxable > 0) {
            calcTax += taxable * TAX_RANGE_SECOND_PERCENTAGE / 100;
            return calcTax;
        }
        taxable = taxable - taxRangeSecond;
        int taxRangeThird = taxRangeSecond + TAX_STEP_THIRD;
        if (taxable > taxRangeThird) {
            calcTax += taxRangeThird * TAX_RANGE_THIRD_PERCENTAGE / 100;
        } else if (taxable > 0) {
            calcTax += taxable * TAX_RANGE_THIRD_PERCENTAGE / 100;
            return calcTax;
        }
        taxable = taxable - taxRangeThird;
        int taxRangeFourth = taxRangeThird + TAX_STEP_FOURTH;
        if (taxable > taxRangeFourth) {
            calcTax += taxRangeFourth * TAX_RANGE_FOURTH_PERCENTAGE / 100;
        } else if (taxable > 0) {
            calcTax += taxable * taxRangeFourth / 100;
            return calcTax;
        }
        taxable = taxable - taxRangeFourth;
        int taxRangeFifth = taxRangeFourth + TAX_STEP_FIFTH;
        if (taxable > taxRangeFifth) {
            calcTax += taxRangeFifth * TAX_RANGE_FIFTH_PERCENTAGE / 100;
        } else if (taxable > 0) {
            calcTax += taxable * TAX_RANGE_FIFTH_PERCENTAGE / 100;
            return calcTax;
        }

        taxable = taxable - taxRangeFifth;
        if (taxable > 0) {
            calcTax += taxable * TAX_RANGE_SIXTH_PERCENTAGE / 100;
            return calcTax;
        }

        return calcTax;
    }
}