package com.example.cesarchretien.kotlinmeetupapp

import android.support.v7.widget.RecyclerView

/**
 * Created by cesarchretien on 15/02/2018.
 */

class CustomDataObserver : RecyclerView.AdapterDataObserver() {

    private var onChangedFunction: RecyclerView.AdapterDataObserver.() -> Unit = { /*do nothing*/ }
    private var onItemRangeChangedFunction: RecyclerView.AdapterDataObserver.(Int, Int) -> Unit = { _, _ -> /*do nothing*/ }
    private var onItemRangeChangedWithPayloadFunction: RecyclerView.AdapterDataObserver.(Int, Int, Any?) -> Unit = { positionStart, itemCount, _ -> onItemRangeChangedFunction(positionStart, itemCount) }
    private var onItemRangeInsertedFunction: RecyclerView.AdapterDataObserver.(Int, Int) -> Unit = { _, _ -> /*do nothing*/ }
    private var onItemRangeRemovedFunction: RecyclerView.AdapterDataObserver.(Int, Int) -> Unit = { _, _ -> /*do nothing*/ }
    private var onItemRangeMovedFunction: RecyclerView.AdapterDataObserver.(Int, Int, Int) -> Unit = { _, _, _ -> /*do nothing*/ }

    override fun onChanged() = onChangedFunction()

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) = onItemRangeChangedFunction(positionStart, itemCount)

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) = onItemRangeChangedWithPayloadFunction(positionStart, itemCount, payload)

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) = onItemRangeRemovedFunction(positionStart, itemCount)

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) = onItemRangeMovedFunction(fromPosition, toPosition, itemCount)

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = onItemRangeInsertedFunction(positionStart, itemCount)

    fun onChanged(f: RecyclerView.AdapterDataObserver.() -> Unit): CustomDataObserver = this.apply {
        onChangedFunction = f
    }

    fun onItemRangeChanged(f: RecyclerView.AdapterDataObserver.(Int, Int) -> Unit) = this.apply {
        onItemRangeChangedFunction = f
    }

    fun onItemRangeChanged(f: RecyclerView.AdapterDataObserver.(Int, Int, Any?) -> Unit) = this.apply {
        onItemRangeChangedWithPayloadFunction = f
    }

    fun onItemRangeInserted(f: RecyclerView.AdapterDataObserver.(Int, Int) -> Unit) = this.apply {
        onItemRangeInsertedFunction = f
    }

    fun onItemRangeRemoved(f: RecyclerView.AdapterDataObserver.(Int, Int) -> Unit) = this.apply {
        onItemRangeRemovedFunction = f
    }

    fun onItemRangeMoved(f: RecyclerView.AdapterDataObserver.(Int, Int, Int) -> Unit) = this.apply {
        onItemRangeMovedFunction = f
    }
}