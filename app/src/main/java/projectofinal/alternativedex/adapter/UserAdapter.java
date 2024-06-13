package projectofinal.alternativedex.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import projectofinal.alternativedex.R;
import projectofinal.alternativedex.listeners.UsersListener;
import projectofinal.alternativedex.models.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private final List<User> users;
    private final UsersListener usersListener;

    public UserAdapter(List<User> users, UsersListener usersListener) {
        this.users = users;
        this.usersListener = usersListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.setUserData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageProfile;
        private TextView textName;

        UserViewHolder(View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.imageProfile);
            textName = itemView.findViewById(R.id.textName);
        }

        void setUserData(User user) {
            textName.setText(user.name);
            Bitmap bitmap = getUserImage(user.image);
            if (bitmap != null) {
                imageProfile.setImageBitmap(bitmap);
            } else {
                imageProfile.setImageResource(R.drawable.default_profile_image); // Set default image if decoding fails
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (usersListener != null) {
                        usersListener.onUserClicked(user);
                    }
                }
            });
        }

        private Bitmap getUserImage(String encodedImage) {
            try {
                byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
