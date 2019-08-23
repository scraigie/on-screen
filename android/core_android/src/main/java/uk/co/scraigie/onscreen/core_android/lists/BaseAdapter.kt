package uk.co.scraigie.onscreen.core_android.lists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class ListItemContent(val type: Int)

abstract class BaseViewHolder<T: ListItemContent>(parent: ViewGroup, @LayoutRes layoutId: Int) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(layoutId, parent, false)) {

    @Suppress("UNCHECKED_CAST")
    fun onBind(item: ListItemContent) {
        (item as? T)?.let {
            bind(it)
        }
    }

    open fun onViewRecycled(items: List<*>) { }

    protected abstract fun bind(item: T)
}


abstract class BaseAdapter<T : ListItemContent, VH: BaseViewHolder<out T>>: RecyclerView.Adapter<VH>() {

    var items: List<T> = emptyList()

    protected abstract val viewHoldersMap: Map<Int, (ViewGroup) -> BaseViewHolder<out T>>

    override fun getItemViewType(position: Int): Int =
        items[position].type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        viewHoldersMap[viewType]?.invoke(parent) as? VH
            ?: throw TypeCastException("Unable to cast ViewHolder of type $viewType to generic VH type")

    override fun onViewRecycled(holder: VH) {
        holder.onViewRecycled(items)
        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int =
        items.size

    override fun onBindViewHolder(holder: VH, position: Int) =
        holder.onBind(items[position])
}