package com.pooja.khanakhalo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SidebarAdapter extends RecyclerView.Adapter<SidebarAdapter.UserViewHolder> {

    private List<String> titleList;
    private List<String> subtitleList;
    private List<Integer> iconList;
    private DrawerLayout drawer;
    private Context context;

    public SidebarAdapter(Context ctx, DrawerLayout drawer) {
        this.context = ctx;
        this.drawer = drawer;
        this.titleList = new ArrayList<>();
        this.iconList = new ArrayList<>();
        this.subtitleList = new ArrayList<>();
        titleList.add("Join Us");titleList.add("Privacy Policy");titleList.add("Share App");titleList.add("Contact Us");titleList.add("Rate Us");titleList.add("Terms and Conditions");titleList.add("About Us");
        iconList.add(R.drawable.ic_vendor);iconList.add(R.drawable.lock);iconList.add(R.drawable.share);iconList.add(R.drawable.email);iconList.add(R.drawable.rate);iconList.add(R.drawable.term);iconList.add(R.drawable.info);
        subtitleList.add("Join as a Vendor");subtitleList.add("Terms & Policies");subtitleList.add("Help us to build big family");subtitleList.add("Share your thought with us");subtitleList.add("Give your rate and feedback");subtitleList.add("Terms and Conditions");subtitleList.add("About Us");
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.lsv_menu,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, final int position) {
        holder.image.setImageResource(iconList.get(position));
        holder.title.setText(titleList.get(position));
        holder.sub_title.setText(subtitleList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performClick(context,drawer,titleList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView sub_title;
        public UserViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            sub_title = itemView.findViewById(R.id.sub_title);
        }
    }

    private static void performClick(Context ctx,DrawerLayout drawer,String title){
        drawer.closeDrawer(GravityCompat.START);
        switch (title) {
            case "Join Us": {
                Intent policy = new Intent(Intent.ACTION_VIEW);
                policy.setData(Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSe8NQkJ52cEgE27Dsnsi6RnAhIdjEBMW0ea2S7WjTOMHvxZgw/viewform?usp=pp_url"));
                ctx.startActivity(policy);
                break;
            }
            case "Privacy Policy": {
                Intent policy = new Intent(Intent.ACTION_VIEW);
                policy.setData(Uri.parse("https://khanaforyou.blogspot.com/p/khana-khalo-privacy-policy.html"));
                ctx.startActivity(policy);
                break;
            }
            case "Share App": {
                try {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.putExtra(Intent.EXTRA_SUBJECT,ctx.getResources().getString(R.string.app_name));
                    String msg = "\nLet me recommend you this application\n\nhttps://play.google.com/store/apps/details?id=" + ctx.getPackageName();
                    share.putExtra(Intent.EXTRA_TEXT, msg);
                    ctx.startActivity(Intent.createChooser(share, "Share Via"));
                } catch(Exception e) {
                    e.fillInStackTrace();
                }
                break;
            }
            case "Contact Us": {
                Intent c = new Intent(Intent.ACTION_VIEW);
                c.setData(Uri.parse("https://khanaforyou.blogspot.com/p/contact.html"));
                ctx.startActivity(c);
                break;
            }
            case "Rate Us": {
                Intent policy = new Intent(Intent.ACTION_VIEW);
                policy.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + ctx.getPackageName()));
                ctx.startActivity(policy);
                break;
            }
            case "Terms and Conditions": {
                Intent policy = new Intent(Intent.ACTION_VIEW);
                policy.setData(Uri.parse("https://khanaforyou.blogspot.com/p/terms-and-conditions.html"));
                ctx.startActivity(policy);
                break;
            }
            case "About Us": {
                Intent policy = new Intent(Intent.ACTION_VIEW);
                policy.setData(Uri.parse("https://khanaforyou.blogspot.com"));
                ctx.startActivity(policy);
                break;
            }
        }
    }

}