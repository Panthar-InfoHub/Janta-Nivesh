package org.velvetinvesting.jantanivesh.app.features.core.domain

/**
 * The goal types the `v2.0` engine knows, keyed by the `goal_type_id` every goal endpoint speaks
 * in. `GET /user-goal/config` is the source of truth for names, rates and tenures; these entries
 * exist so the client can decide *which fields to collect* for a type without waiting on config,
 * and so an unknown id coming back from the server can be told apart from a known one.
 */
enum class GoalType(val id: Int, val displayName: String) {
    ChildEducation(1, "Child's Education"),
    ChildMarriage(2, "Child's Marriage"),
    BuyHome(3, "Buy a Home"),
    BuyVehicle(4, "Buy a Vehicle"),
    BuildSavings(5, "Build My Savings"),
    OtherGoal(6, "Other Goal");

    /** Types 1 and 2 are titled from the child rather than from a name the user types. */
    val needsChildDetails: Boolean
        get() = this == ChildEducation || this == ChildMarriage

    /** What follows the child's name in a title: "Education", as in "Aarav's Education". */
    val childGoalNoun: String
        get() = displayName.removePrefix("Child's ")

    /** Every other type carries a user-chosen `goal_name`. */
    val needsGoalName: Boolean
        get() = !needsChildDetails

    /** Only the vehicle goal has variants, and config lists which ones. */
    val needsAssetSubtype: Boolean
        get() = this == BuyVehicle

    /**
     * "Build My Savings" is the one type sized from a corpus the user names outright, so it sends
     * `target_amount`; the rest send a present-day `current_cost` for the server to inflate.
     */
    val usesTargetAmount: Boolean
        get() = this == BuildSavings

    companion object {
        fun fromId(id: Int?): GoalType? = entries.firstOrNull { it.id == id }
    }
}
