import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.loanapplication.data.model.Loan
import com.example.loanapplication.databinding.LoanHistoryItemBinding


class LoanHistoryAdeptor(
) : ListAdapter<Loan, LoanHistoryAdeptor.ViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Loan>() {
            override fun areItemsTheSame(oldItem: Loan, newItem: Loan): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Loan, newItem: Loan): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LoanHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: LoanHistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")

        fun bind(item: Loan) {
            with(binding) {
                tvLoanAmount.text = item.amount.toString()
                tvLoanDuration.text = item.duration.toString()
                tvLoanStatus.text = item.status
            }

            fun updateData(newList: List<Loan>) {
                Log.d("OTLightAdeptor", "Updating data: $newList")
                submitList(newList)
            }
        }
    }
    }
