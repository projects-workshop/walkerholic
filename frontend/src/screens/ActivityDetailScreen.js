import React, { useEffect } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { getActivity } from '../_actions/ActivityActions'
import earth from '../images/earth.svg'
import ActivityUserCard from '../components/activity/ActivityUserCard'
import Error from '../components/Error'
import Loading from '../components/Loading'


function ActivityDetailScreen(props) {

    const id = props.match.params.id

    const activity = useSelector(state => state.activity)

    const dispatch = useDispatch()

    useEffect(() => {
        dispatch(getActivity(id))
    }, [dispatch])

    return (
        <div className="activity_detail">
            {
                activity.error && activity.error.message && <Error error = {activity.error.message}/>
            }
            {
                activity.Loading && <Loading/>
            }
            {
                activity.activity?.id &&
                <>
                    <div className="activity_detail_info">
                        <div className="activity_detail_image">
                            <img src={activity.activity.imageUrl?activity.activity.imageUrl : earth} alt="activityImage"/>
                        </div>
                        <div className="activity_detail_activity_info">
                            <div style={{textAlign:"center",fontWeight:"800",fontSize:"2rem"}}>
                                {activity.activity.name}
                            </div>
                            <div style={{textAlign:"right",fontWeight:"600", fontSize:"15px"}}>
                                <strong>{activity.activity.score}</strong> points activity
                            </div>
                            <div style={{fontSize:'13px', color:"#737373", fontWeight:"600"}}>
                                {activity.activity.description}
                            </div>
                        </div>
                    </div>

                    <div className="activityuser_user_title">
                        People who participated in this activity : )
                    </div>

                    <div className="activityuser_card_container">
                        {
                            activity.activity?.activityUsers.map((activity,index)=>(
                                <ActivityUserCard activity={activity} key={index}/>
                            ))
                        }
                    </div>
                </>
            }
        </div>
    )
}

export default ActivityDetailScreen