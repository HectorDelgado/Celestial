package com.hectordelgado.celestial.feature.core.topbar

class TopBarState private constructor(
    val isVisible: Boolean,
    val title: String,
    val leftIcon: TopBarLeftIcon?,
    val rightIcons: List<TopBarRightIcon>
) {
    companion object {
        val empty = TopBarState(
            isVisible = false,
            title = "",
            leftIcon = null,
            rightIcons = emptyList()
        )
    }
    class Builder(
        private var isVisible: Boolean = false,
        private var title: String = "",
        private var leftIcon: TopBarLeftIcon? = null,
        private var rightIcons: List<TopBarRightIcon> = emptyList()

    ) {
        constructor(state: TopBarState) : this(
            isVisible = state.isVisible,
            title = state.title,
            leftIcon = state.leftIcon,
            rightIcons = state.rightIcons
        )

        fun setIsVisible(isVisible: Boolean) = apply { this.isVisible = isVisible }
        fun setTitle(title: String) = apply { this.title = title }
        fun setLeftIcon(leftIcon: TopBarLeftIcon?) = apply { this.leftIcon = leftIcon }
        fun setRightIcons(rightIcons: List<TopBarRightIcon>) = apply { this.rightIcons = rightIcons }

        fun build(): TopBarState {
            return TopBarState(
                isVisible = isVisible,
                title = title,
                leftIcon = leftIcon,
                rightIcons = rightIcons
            )
        }
    }
}

enum class TopBarLeftIcon {
    BACK,
    CLOSE
}

enum class TopBarRightIcon {
    OPTIONS
}