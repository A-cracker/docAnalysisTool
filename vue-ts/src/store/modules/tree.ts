interface TreeModuleState {
    treeData: Object;
}

const treeModule = {
    namespaced: true,
    state: (): TreeModuleState => ({
        treeData: {},
    }),
    mutations: {
        setTreeData(state: TreeModuleState,treeData: Object){
            state.treeData = treeData
        }
    },
    getters:{
        getTreeData(state: TreeModuleState){
            return state.treeData
        }
    },
    actions: {
        incrementAsync({ commit }: any) {
            setTimeout(() => {
                commit('setTreeData');
            }, 1000);
        },
    },
};

export default treeModule;



