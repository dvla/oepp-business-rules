package uk.gov.dvla.rules;

import com.google.common.base.MoreObjects;
import uk.gov.dvla.domain.OffenceCase;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

@SuppressWarnings("unused")
/**
 * Processing context used by rules engine to track processing state.
 */
public class ProcessingContext {

    private final OffenceCase offenceCase;
    // processing result
    private Optional<RejectionReason> rejectionReason;
    private Optional<PenaltyType> penaltyType;
    private Optional<HGVLevy> hgvLevy;

    /**
     * Initiates processing context with input object of type {@link OffenceCase}.
     * <p>
     * Remaining fields which represents processing state are initialised with reasonable default values.
     *
     * @param offenceCase input object
     */
    public ProcessingContext(OffenceCase offenceCase) {
        this.offenceCase = offenceCase;
        this.rejectionReason = Optional.empty();
        this.penaltyType = Optional.empty();
        this.hgvLevy = Optional.empty();
    }

    public OffenceCase getOffenceCase() {
        return offenceCase;
    }

    public Optional<RejectionReason> getRejectionReason() {
        return rejectionReason;
    }

    public Optional<PenaltyType> getPenaltyType() {
        return penaltyType;
    }

    public Optional<HGVLevy> getHgvLevy() { return hgvLevy; }

    /**
     * Changes processing state to mark offence case as rejected.
     * <p>
     * From that point on other state changes are forbidden and will result in throwing {@link IllegalStateException}.
     *
     * @param reason rejection reason, must be not null
     * @return current processing state which allows methods chaining
     */
    public ProcessingContext reject(RejectionReason reason) {
        checkNotNull(reason, "Rejection reason is required");
        rejectionReason = Optional.of(reason);
        return this;
    }

    /**
     * Sets the penalty type.
     *
     * @throws IllegalStateException when offence case has been rejected before.
     * @return current processing state which allows methods chaining
     */
    public ProcessingContext setPenaltyType(PenaltyType penaltyType) {
        checkState(!rejectionReason.isPresent(), "Offence case has already been rejected");
        this.penaltyType = Optional.of(penaltyType);
        return this;
    }

    public ProcessingContext setHGVLevy(HGVLevy hgvLevy) {
        this.hgvLevy = Optional.of(hgvLevy);
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("offenceCase", offenceCase)
                .add("rejectionReason", rejectionReason)
                .add("penaltyType", penaltyType)
                .add("hgvLevy", hgvLevy)
                .toString();
    }

}
