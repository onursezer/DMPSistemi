package com.bitirme2.onursezer.dmpsistemi;

/**
 * Created by NgocTri on 3/11/2017.
 */

/*public class ImageListAdapter extends ArrayAdapter<ImageUpload> {
    private Activity context;
    private int resource;
    private List<ImageUpload> listImage;

    public ImageListAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<ImageUpload> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listImage = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View v = inflater.inflate(resource, null);
        TextView tvName = (TextView) v.findViewById(tvImageName);
        ImageView img = (ImageView) v.findViewById(R.id.imgView);

        tvName.setText(listImage.get(position).getName());
        Glide.with(context).load(listImage.get(position).getUrl()).into(img);

        return v;

    }
}*/
