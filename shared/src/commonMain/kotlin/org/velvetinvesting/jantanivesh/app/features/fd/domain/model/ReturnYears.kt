package org.velvetinvesting.jantanivesh.app.features.fd.domain.model

sealed interface ReturnYears {
    val years: Int
    val displayName: String
    data object Year1: ReturnYears{
        override val years = 1
        override val displayName = "Returns (1Y)"
    }
    data object Year2 : ReturnYears{
        override val years = 2
        override val displayName = "Returns (2Y)"
    }
    data object Year3 : ReturnYears{
        override val years = 3
        override val displayName = "Returns (3Y)"
    }
    data object Year4 : ReturnYears{
        override val years = 4
        override val displayName = "Returns (4Y)"
    }
    data object Year5 : ReturnYears{
        override val years = 5
        override val displayName = "Returns (5Y)"
    }
}