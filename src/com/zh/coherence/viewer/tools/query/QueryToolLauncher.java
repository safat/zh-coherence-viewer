package com.zh.coherence.viewer.tools.query;

import com.zh.coherence.viewer.tableview.CoherenceTableView;
import com.zh.coherence.viewer.tableview.RightButtonMenuBuilder;
import com.zh.coherence.viewer.tableview.user.UserObjectViewer;
import com.zh.coherence.viewer.tools.CoherenceViewerTool;
import com.zh.coherence.viewer.tools.ToolLauncher;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

public class QueryToolLauncher extends ToolLauncher implements InitializingBean{

    private List<UserObjectViewer> viewers;

    private RightButtonMenuBuilder viewerMenuBuilder;

    @Override
    public CoherenceViewerTool newTool() {
        CoherenceTableView tableView = new CoherenceTableView(viewers, viewerMenuBuilder);
        return new QueryTool(tableView);
    }

    public List<UserObjectViewer> getViewers() {
        return viewers;
    }

    public void setViewers(List<UserObjectViewer> viewers) {
        this.viewers = viewers;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(viewers == null){
            throw new IllegalStateException("viewers couldn't be null");
        }
        if(viewerMenuBuilder == null){
            throw new IllegalStateException("viewerMenuBuilder couldn't be null");
        }
    }

    public RightButtonMenuBuilder getViewerMenuBuilder() {
        return viewerMenuBuilder;
    }

    public void setViewerMenuBuilder(RightButtonMenuBuilder viewerMenuBuilder) {
        this.viewerMenuBuilder = viewerMenuBuilder;
    }
}
