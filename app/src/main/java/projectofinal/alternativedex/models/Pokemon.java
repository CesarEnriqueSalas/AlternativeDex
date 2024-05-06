package projectofinal.alternativedex.models;

import android.content.Context;
import android.content.Intent;

import projectofinal.alternativedex.activities.DetailActivity;

public class Pokemon {

    private String name;
    private String url;
    private String generation;
    public String getName() {
        return name;
    }

    public String getGeneration() {
        return generation;
    }

    public void setGeneration(String generation) {
        this.generation = generation;
    }

    public void setName(String name) { this.name = name; }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNumberPNG() {
        String[] urlPartes = url.split("/");
        return Integer.parseInt(urlPartes[urlPartes.length - 1]);
    }

    public Intent getIntent(Context context){
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("url", url);
        intent.putExtra("numero", getNumberPNG());

        return intent;
    }

}
