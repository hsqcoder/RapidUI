package pers.like.framework.main.component.zxing.view;

import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;

/**
 * @author like
 */
public final class ViewfinderResultPointCallback implements ResultPointCallback {

    private final ViewfinderView viewfinderView;

    public ViewfinderResultPointCallback(ViewfinderView viewfinderView) {
        this.viewfinderView = viewfinderView;
    }

    @Override
    public void foundPossibleResultPoint(ResultPoint point) {
        viewfinderView.addPossibleResultPoint(point);
    }

}
