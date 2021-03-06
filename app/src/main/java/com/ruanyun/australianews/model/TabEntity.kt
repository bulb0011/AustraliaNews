package com.ruanyun.australianews.model

import com.flyco.tablayout.listener.CustomTabEntity

class TabEntity : CustomTabEntity {
    var title: String
    lateinit var type: String
    var selectedIcon: Int = 0
    var unSelectedIcon: Int = 0

    constructor(title: String) {
        this.title = title
    }

    constructor(title: String, type: String) {
        this.title = title
        this.type = type
    }

    constructor(title: String, selectedIcon: Int, unSelectedIcon: Int) {
        this.title = title
        this.selectedIcon = selectedIcon
        this.unSelectedIcon = unSelectedIcon
    }

    override fun getTabTitle(): String {
        return title
    }

    override fun getTabSelectedIcon(): Int {
        return selectedIcon
    }

    override fun getTabUnselectedIcon(): Int {
        return unSelectedIcon
    }
}
