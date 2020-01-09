package org.tasks.tags;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import kotlin.jvm.functions.Function2;
import org.tasks.R;
import org.tasks.billing.Inventory;
import org.tasks.data.TagData;
import org.tasks.themes.CustomIcons;
import org.tasks.themes.ThemeCache;

public class TagRecyclerAdapter extends RecyclerView.Adapter<TagPickerViewHolder> {

  private final AsyncListDiffer<TagData> differ;
  private final Context context;
  private final TagPickerViewModel viewModel;
  private final ThemeCache themeCache;
  private final Inventory inventory;
  private final Function2<TagData, Boolean, Void> callback;

  TagRecyclerAdapter(
      Context context,
      TagPickerViewModel viewModel,
      ThemeCache themeCache,
      Inventory inventory,
      Function2<TagData, Boolean, Void> callback) {
    this.context = context;
    this.viewModel = viewModel;
    this.themeCache = themeCache;
    this.inventory = inventory;
    this.callback = callback;
    differ = new AsyncListDiffer<>(this, new TagDiffCallback());
  }

  @NonNull
  @Override
  public TagPickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.row_tag_picker, parent, false);
    return new TagPickerViewHolder(context, view, callback);
  }

  @Override
  public void onBindViewHolder(@NonNull TagPickerViewHolder holder, int position) {
    TagData tagData = differ.getCurrentList().get(position);
    boolean checked = viewModel.isChecked(tagData);
    holder.bind(tagData, getColor(tagData), getIcon(tagData), checked);
  }

  @Override
  public int getItemCount() {
    return differ.getCurrentList().size();
  }

  private int getColor(TagData tagData) {
    return themeCache
        .getThemeColor(tagData.getColor() >= 0 ? tagData.getColor() : 19)
        .getPrimaryColor();
  }

  private @Nullable Integer getIcon(TagData tagData) {
    return tagData.getIcon() < 1000 || inventory.hasPro()
        ? CustomIcons.getIconResId(tagData.getIcon())
        : null;
  }

  public void submitList(List<TagData> tagData) {
    differ.submitList(tagData);
  }
}