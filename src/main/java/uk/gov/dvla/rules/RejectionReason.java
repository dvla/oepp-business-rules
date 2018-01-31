package uk.gov.dvla.rules;

public enum RejectionReason {
    INVALID_STATE,
    NO_PAYMENT_REQUIRED,
    PENALTY_ALREADY_PAID,
    PASSED_TO_DEBT_COLLECTORS,
    PASSED_TO_COURT
}
