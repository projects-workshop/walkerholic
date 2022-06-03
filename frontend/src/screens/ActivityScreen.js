import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import ActivityCard from '../components/activity/ActivityCard'
import EditActivity from '../components/activity/EditActivity'
import Error from '../components/Error'
import Level from '../components/Level'
import Loading from '../components/Loading'
import { getActivities } from '../_actions/ActivityActions'

function ActivityScreen() {

    const auth = useSelector(state => state.auth)
    const activity = useSelector(state => state.activity)

    const dispatch = useDispatch()

    useEffect(() => {
        dispatch(getActivities())
    }, [dispatch])

    const [isCreate, setIsCreate] = useState(false)
    
    return (
        <div className="activity">
            <Level/>
            {
                auth && auth.user && auth.user.role === "ADMIN" &&
                <div style={{marginTop:"4rem", textAlign:"right", marginRight:"4rem"}}>
                    <button onClick={()=>setIsCreate(!isCreate)}>Create Activity</button>
                </div>
            }
            {
                activity.error && activity.error.message && <Error error = {activity.error.message}/>
            }
            {
                activity.Loading && <Loading/>
            }
            {
                activity.loading===false &&
                <div>
                    {
                        activity.activities?.map((activity, index)=>(
                            <ActivityCard activity={activity} key={index}/>
                        ))
                    }
                </div>
            }

            {
                isCreate &&
                <EditActivity  setIsEdit={setIsCreate}/>
            }


        </div>
    )
}

export default ActivityScreen
